package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.ReconciliationAction;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction.ActionType;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.validators.reconciliation.VsyncHistoricValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class FailureReconciliationSyncUseCase {

    private static final String LOG_START_TIME = "startCurrentTimeMillis";
    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final FindPixKeyPort findPixKeyPort;
    private final SyncVerifierPort syncVerifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final SyncVerifierHistoricActionPort syncVerifierHistoricActionPort;
    private final CreatePixKeyPort createPixKeyPort;
    private final UpdateAccountPixKeyPort updateAccountPixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;

    private SyncVerifierHistoric syncVerifierHistoric;

    public void execute(SyncVerifierHistoric syncVerifierHistoric) {
        this.syncVerifierHistoric = syncVerifierHistoric;
        long startCurrentTimeMillis = System.currentTimeMillis();
        log.info("FailureReconciliationSync_started: {}, {}",
            kv(LOG_START_TIME, startCurrentTimeMillis),
            kv("vsyncHistoric", syncVerifierHistoric));

        VsyncHistoricValidator.validate(syncVerifierHistoric);

        Set<BacenCidEvent> bacenEvents = bacenContentIdentifierEventsPort.list(syncVerifierHistoric.getKeyType(),
            syncVerifierHistoric.getSynchronizedStart());
        Set<BacenCidEvent> latestBacenEvents = syncVerifierHistoric.groupBacenEventsByCidMaxByDate(bacenEvents);
        latestBacenEvents.forEach(bacenCidEvent -> {
            var pixKeyInDatabase = findPixKeyPort.findPixKeyByCid(bacenCidEvent.getCid());
            if (ReconciliationAction.ADDED.equals(bacenCidEvent.getAction())) {
                if (pixKeyInDatabase.isEmpty()) createOrUpdatePixKey(bacenCidEvent);
            } else {
                pixKeyInDatabase.ifPresent(this::removePixKey);
            }
        });

        performSyncVerifier(syncVerifierHistoric, bacenEvents);

        log.info("FailureReconciliationSync_ended: {}, {}",
            kv(LOG_START_TIME, startCurrentTimeMillis),
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
            .ifPresent(pixKey -> {
                findPixKeyPort.findPixKey(pixKey.getKey()).ifPresentOrElse(
                    pixKeyInDatabase -> {
                        updateAccountPixKeyPort.updateAccount(pixKey, UpdateReason.RECONCILIATION);
                        createHistoricAction(pixKeyInDatabase.getCid(), ActionType.REMOVE);
                        createHistoricAction(pixKey.getCid(), ActionType.ADD);
                    },
                    () -> {
                        createPixKeyPort.createPixKey(pixKey, CreateReason.RECONCILIATION);
                        createHistoricAction(pixKey.getCid(), ActionType.ADD);
                    });
            });
    }

    private void createHistoricAction(final String cid, final ActionType actionType) {
        var historicAction = SyncVerifierHistoricAction.builder()
            .cid(cid)
            .actionType(actionType)
            .syncVerifierHistoric(syncVerifierHistoric)
            .build();
        syncVerifierHistoricActionPort.save(historicAction);

        log.info("FailureReconciliationSync_changePixKey: {}, {}, {}",
            kv("cid", cid),
            kv("actionType", actionType),
            kv("vsyncHistoric", syncVerifierHistoric));
    }

    private void removePixKey(final PixKey pixKey) {
        removePixKeyPort.remove(pixKey.getKey(), pixKey.getIspb());
        createHistoricAction(pixKey.getCid(), ActionType.REMOVE);
    }

}
