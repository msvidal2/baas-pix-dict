package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.domain.ReconciliationAction;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.validators.reconciliation.VsyncHistoricValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class FailureReconciliationSyncUseCase {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final FindPixKeyPort findPixKeyPort;
    private final SyncVerifierPort syncVerifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final SyncVerifierHistoricActionPort syncVerifierHistoricActionPort;
    private final SavePixKeyPort savePixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;
    private final PixKeyEventPort pixKeyEventPort;

    private SyncVerifierHistoric syncVerifierHistoric;

    public void execute(SyncVerifierHistoric syncVerifierHistoric) {
        this.syncVerifierHistoric = syncVerifierHistoric;
        var startCurrentTimeMillis = System.currentTimeMillis();
        log.info("FailureReconciliationSync_started: {}",
            kv("vsyncHistoric", syncVerifierHistoric));

        VsyncHistoricValidator.validate(syncVerifierHistoric);

        Set<BacenCidEvent> bacenEvents = bacenContentIdentifierEventsPort.list(syncVerifierHistoric.getKeyType(),
            syncVerifierHistoric.getSynchronizedStart());
        Set<BacenCidEvent> latestBacenEvents = syncVerifierHistoric.groupBacenEventsByCidMaxByDate(bacenEvents);
        latestBacenEvents.forEach(bacenCidEvent -> {
            var pixKeyInDatabase = findPixKeyPort.findByCid(bacenCidEvent.getCid());
            if (ReconciliationAction.ADDED.equals(bacenCidEvent.getAction())) {
                if (pixKeyInDatabase.isEmpty()) createOrUpdatePixKey(bacenCidEvent);
            } else {
                pixKeyInDatabase.ifPresent(this::removePixKey);
            }
        });

        performSyncVerifier(syncVerifierHistoric, bacenEvents);

        log.info("FailureReconciliationSync_ended: {}, {}",
            kv("vsyncHistoric", syncVerifierHistoric),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / 1000));
    }

    private void performSyncVerifier(final SyncVerifierHistoric syncVerifierHistoric, final Set<BacenCidEvent> bacenEvents) {
        var syncVerifier = syncVerifierPort.getLastSuccessfulVsync(syncVerifierHistoric.getKeyType()).orElseThrow();
        var vsyncCurrent = syncVerifier.calculateVsync(bacenEvents.stream().map(BacenCidEvent::getCid).collect(Collectors.toList()));
        var syncVerifierResult = bacenSyncVerificationsPort.syncVerification(syncVerifierHistoric.getKeyType(), vsyncCurrent);
        var newSyncVerifierHistoric = syncVerifier.syncVerificationResult(vsyncCurrent, syncVerifierResult);
        syncVerifierPort.save(syncVerifier);
        syncVerifierHistoricPort.save(newSyncVerifierHistoric);

        log.info("FailureReconciliationSync_performSyncVerifier: {}, {}",
            kv("vsyncHistoric", syncVerifierHistoric),
            kv("newSyncVerifierHistoric", newSyncVerifierHistoric));
    }

    private void createOrUpdatePixKey(final BacenCidEvent bacenCidEvent) {
        bacenContentIdentifierEventsPort.getPixKey(bacenCidEvent.getCid())
            .ifPresent(pixKey -> findPixKeyPort.findPixKey(pixKey.getKey()).ifPresentOrElse(
                pixKeyInDatabase -> {
                    savePixKeyPort.savePixKey(pixKey, Reason.RECONCILIATION);
                    createHistoricAction(pixKeyInDatabase.getCid(), ReconciliationAction.REMOVED);
                    createHistoricAction(pixKey.getCid(), ReconciliationAction.ADDED);
                    pixKeyEventPort.pixKeyWasUpdatedByReconciliation(pixKey);
                }, () -> {
                    savePixKeyPort.savePixKey(pixKey, Reason.RECONCILIATION);
                    createHistoricAction(pixKey.getCid(), ReconciliationAction.ADDED);
                    pixKeyEventPort.pixKeyWasCreatedByReconciliation(pixKey);
                }));
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
            kv("vsyncHistoric", syncVerifierHistoric));
    }

    private void removePixKey(final PixKey pixKey) {
        removePixKeyPort.removeByCid(pixKey.getCid());
        createHistoricAction(pixKey.getCid(), ReconciliationAction.REMOVED);
        pixKeyEventPort.pixKeyWasRemovedByReconciliation(pixKey.toBuilder().updatedAt(LocalDateTime.now()).build());
    }

}
