package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoricAction;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.validators.reconciliation.VsyncHistoricValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Stream;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class AggregateReconciliationCheckUseCase {

    private static final String SYNC_VERIFIER_HISTORIC = "syncVerifierHistoric";
    private final ContentIdentifierPort contentIdentifierPort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final FindPixKeyPort findPixKeyPort;
    private final SyncVerifierPort syncVerifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final ReconciliationActionPort reconciliationActionPort;

    private SyncVerifierHistoric failureSyncVerifier;

    public SyncVerifierHistoric execute(SyncVerifierHistoric failureSyncVerifier) {
        var startCurrentTimeMillis = System.currentTimeMillis();
        log.info("FailureReconciliationSync_started: {}",
            kv(SYNC_VERIFIER_HISTORIC, failureSyncVerifier));

        VsyncHistoricValidator.validate(failureSyncVerifier);
        this.failureSyncVerifier = failureSyncVerifier;

        Set<BacenCidEvent> bacenEvents = contentIdentifierPort.findBacenEventsAfter(failureSyncVerifier.getKeyType(),
            failureSyncVerifier.getSynchronizedStart());
        Set<BacenCidEvent> latestBacenEvents = failureSyncVerifier.groupBacenEventsByCidMaxByDate(bacenEvents);

        Runnable addedEventsProcessor = () -> latestBacenEvents.parallelStream()
            .filter(
                bacenCidEvent -> bacenCidEvent.getAction() == ReconciliationAction.ADDED
                    && findPixKeyPort.findByCid(bacenCidEvent.getCid()).isEmpty())
            .flatMap(bacenCidEvent -> bacenPixKeyByContentIdentifierPort.getPixKeyByCid(bacenCidEvent.getCid()).stream())
            .parallel()
            .forEach(pixKeyInBacen -> findPixKeyPort.findPixKey(pixKeyInBacen.getKey())
                .ifPresentOrElse(pixKeyInDatabase -> updatePixKey(pixKeyInDatabase, pixKeyInBacen),
                    () -> insertPixKey(pixKeyInBacen)));

        Runnable removedEventsProcessor = () -> latestBacenEvents.parallelStream()
            .filter(bacenCidEvent -> bacenCidEvent.getAction() == ReconciliationAction.REMOVED)
            .map(bacenCidEvent -> findPixKeyPort.findByCid(bacenCidEvent.getCid()))
            .parallel().forEach(pixKey -> pixKey
                .ifPresent(this::removePixKey));

        Stream.of(addedEventsProcessor, removedEventsProcessor).parallel().forEach(Runnable::run);

        failureSyncVerifier.fixedByAggregateReconciliation();
        syncVerifierHistoricPort.save(failureSyncVerifier);

        var newSyncVerifierHistoric = performSyncVerifier(failureSyncVerifier, bacenEvents);

        final int MILLISECONDS_TO_SECONDS = 1000;
        log.info("FailureReconciliationSync_ended: {}, {}, {}",
            kv(SYNC_VERIFIER_HISTORIC, failureSyncVerifier),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / MILLISECONDS_TO_SECONDS),
            kv("countBacenCidEvents", bacenEvents.size()));

        return newSyncVerifierHistoric;
    }

    private void insertPixKey(final PixKey pixKey) {
        reconciliationActionPort.insertPixKey(createHistoricAction(pixKey, ReconciliationAction.ADDED));
    }

    private void updatePixKey(final PixKey oldPixKey, final PixKey newPixKey) {
        reconciliationActionPort.updatePixKey(createHistoricAction(oldPixKey, ReconciliationAction.REMOVED),
            createHistoricAction(newPixKey, ReconciliationAction.ADDED));
    }

    private void removePixKey(final PixKey pixKey) {
        reconciliationActionPort.removePixKey(createHistoricAction(pixKey, ReconciliationAction.REMOVED));
    }

    private SyncVerifierHistoricAction createHistoricAction(final PixKey pixKey, final ReconciliationAction action) {
        var historicAction = SyncVerifierHistoricAction.builder()
            .cid(pixKey.getCid())
            .pixKey(pixKey)
            .action(action)
            .syncVerifierHistoric(failureSyncVerifier)
            .build();

        log.info("FailureReconciliationSync_changePixKey: {}, {}, {}",
            kv("cid", pixKey.getCid()),
            kv("action", action),
            kv(SYNC_VERIFIER_HISTORIC, failureSyncVerifier));

        return historicAction;
    }

    private SyncVerifierHistoric performSyncVerifier(final SyncVerifierHistoric syncVerifierHistoric, final Set<BacenCidEvent> bacenEvents) {
        var syncVerifier = syncVerifierPort.getLastSuccessfulVsync(syncVerifierHistoric.getKeyType());
        var vsyncCurrent = syncVerifier.calculateVsync(bacenEvents.stream().map(BacenCidEvent::getCid));
        var syncVerifierResult = bacenSyncVerificationsPort.syncVerification(syncVerifierHistoric.getKeyType(), vsyncCurrent);
        var newSyncVerifierHistoric = syncVerifier.syncVerificationResult(vsyncCurrent, syncVerifierResult);
        syncVerifierPort.save(syncVerifier);
        syncVerifierHistoricPort.save(newSyncVerifierHistoric);

        log.info("FailureReconciliationSync_performSyncVerifier: {}, {}",
            kv(SYNC_VERIFIER_HISTORIC, syncVerifierHistoric),
            kv("newSyncVerifierHistoric", newSyncVerifierHistoric));

        return newSyncVerifierHistoric;
    }

}
