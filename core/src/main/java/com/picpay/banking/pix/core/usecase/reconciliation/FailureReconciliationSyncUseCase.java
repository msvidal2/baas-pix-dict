package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoricAction;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncBacenCidEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.validators.reconciliation.VsyncHistoricValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class FailureReconciliationSyncUseCase {

    public static final String SYNC_VERIFIER_HISTORIC = "syncVerifierHistoric";
    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final FindPixKeyPort findPixKeyPort;
    private final SyncVerifierPort syncVerifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final SyncVerifierHistoricActionPort syncVerifierHistoricActionPort;
    private final SavePixKeyPort savePixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;
    private final PixKeyEventPort pixKeyEventPort;
    private final SyncBacenCidEventsPort syncBacenCidEventsPort;

    private SyncVerifierHistoric syncVerifierHistoric;

    public void execute(SyncVerifierHistoric syncVerifierHistoric) {
        this.syncVerifierHistoric = syncVerifierHistoric;
        var startCurrentTimeMillis = System.currentTimeMillis();
        log.info("FailureReconciliationSync_started: {}",
            kv(SYNC_VERIFIER_HISTORIC, syncVerifierHistoric));

        VsyncHistoricValidator.validate(syncVerifierHistoric);

        Set<BacenCidEvent> bacenEvents = syncBacenCidEventsPort.listAfterLastSyncronized(syncVerifierHistoric.getKeyType(),
            syncVerifierHistoric.getSynchronizedStart());
        Set<BacenCidEvent> latestBacenEvents = syncVerifierHistoric.groupBacenEventsByCidMaxByDate(bacenEvents);

        Runnable addedEventsProcessor = () -> latestBacenEvents.parallelStream()
            .filter(
                bacenCidEvent -> bacenCidEvent.getAction() == ReconciliationAction.ADDED
                    && findPixKeyPort.findByCid(bacenCidEvent.getCid()).isEmpty())
            .map(bacenCidEvent -> bacenContentIdentifierEventsPort.getPixKey(bacenCidEvent.getCid()))
            .parallel().forEach(pixKeyInBacen -> pixKeyInBacen.ifPresent(
                pixKey -> findPixKeyPort.findPixKey(pixKey.getKey()).ifPresentOrElse(
                    pixKeyInDatabase -> updatePixKey(pixKeyInDatabase, pixKey),
                    () -> insertPixKey(pixKey))));

        Runnable removedEventsProcessor = () -> latestBacenEvents.parallelStream()
            .filter(bacenCidEvent -> bacenCidEvent.getAction() == ReconciliationAction.REMOVED)
            .map(bacenCidEvent -> findPixKeyPort.findByCid(bacenCidEvent.getCid()))
            .parallel().forEach(pixKey -> pixKey.ifPresent(this::removePixKey));

        Stream.of(addedEventsProcessor, removedEventsProcessor).parallel().forEach(Runnable::run);

        performSyncVerifier(syncVerifierHistoric, bacenEvents);

        final int MILLISECONDS_TO_SECONDS = 1000;
        log.info("FailureReconciliationSync_ended: {}, {}, {}",
            kv(SYNC_VERIFIER_HISTORIC, syncVerifierHistoric),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / MILLISECONDS_TO_SECONDS),
            kv("countBacenCidEvents", bacenEvents.size()));
    }

    private void insertPixKey(final PixKey pixKey) {
        savePixKeyPort.savePixKey(pixKey, Reason.RECONCILIATION);
        createHistoricAction(pixKey.getCid(), ReconciliationAction.ADDED);
        pixKeyEventPort.pixKeyWasCreated(pixKey);
    }

    private void updatePixKey(final PixKey oldPixKey, final PixKey newPixKey) {
        savePixKeyPort.savePixKey(newPixKey, Reason.RECONCILIATION);
        createHistoricAction(oldPixKey.getCid(), ReconciliationAction.REMOVED);
        createHistoricAction(newPixKey.getCid(), ReconciliationAction.ADDED);
        pixKeyEventPort.pixKeyWasUpdated(newPixKey);
    }

    private void removePixKey(final PixKey pixKey) {
        removePixKeyPort.removeByCid(pixKey.getCid());
        createHistoricAction(pixKey.getCid(), ReconciliationAction.REMOVED);
        pixKeyEventPort.pixKeyWasRemoved(pixKey.toBuilder().updatedAt(LocalDateTime.now()).build());
    }

    private void createHistoricAction(final String cid, final ReconciliationAction action) {
        var historicAction = SyncVerifierHistoricAction.builder()
            .cid(cid)
            .action(action)
            .syncVerifierHistoric(syncVerifierHistoric)
            .build();
        syncVerifierHistoricActionPort.save(historicAction);

        log.info("FailureReconciliationSync_changePixKey: {}, {}, {}",
            kv("cid", cid),
            kv("action", action),
            kv(SYNC_VERIFIER_HISTORIC, syncVerifierHistoric));
    }

    private void performSyncVerifier(final SyncVerifierHistoric syncVerifierHistoric, final Set<BacenCidEvent> bacenEvents) {
        var syncVerifier = syncVerifierPort.getLastSuccessfulVsync(syncVerifierHistoric.getKeyType()).orElseThrow();
        var vsyncCurrent = syncVerifier.calculateVsync(bacenEvents.stream().map(BacenCidEvent::getCid).collect(Collectors.toList()));
        var syncVerifierResult = bacenSyncVerificationsPort.syncVerification(syncVerifierHistoric.getKeyType(), vsyncCurrent);
        var newSyncVerifierHistoric = syncVerifier.syncVerificationResult(vsyncCurrent, syncVerifierResult);
        syncVerifierPort.save(syncVerifier);
        syncVerifierHistoricPort.save(newSyncVerifierHistoric);

        log.info("FailureReconciliationSync_performSyncVerifier: {}, {}",
            kv(SYNC_VERIFIER_HISTORIC, syncVerifierHistoric),
            kv("newSyncVerifierHistoric", newSyncVerifierHistoric));
    }

}
