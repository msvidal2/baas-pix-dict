package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifier;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.ReconciliationBacenPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.FailureReconciliationMessagePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@AllArgsConstructor
public class ReconciliationSyncUseCase {

    private final SyncVerifierPort syncVerifierPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final ContentIdentifierEventPort contentIdentifierEventPort;
    private final ReconciliationBacenPort reconciliationBacenPort;
    private final FailureReconciliationMessagePort failureReconciliationMessagePort;

    public void execute() {
        var startCurrentTimeMillis = System.currentTimeMillis();
        log.info("ReconciliationSync_started {}", kv("startCurrentTimeMillis", startCurrentTimeMillis));

        List.of(KeyType.values()).forEach(keyType -> {
            var syncVerifier = syncVerifierPort.getLastSuccessfulVsync(keyType)
                .orElseGet(() -> SyncVerifier.builder()
                    .keyType(keyType)
                    .synchronizedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .build());

            List<ContentIdentifierEvent> contentIdentifiers = contentIdentifierEventPort.findAllAfterLastSuccessfulVsync(syncVerifier.getKeyType(),
                syncVerifier.getSynchronizedAt());

            var vsyncCurrent = syncVerifier.calculateVsync(contentIdentifiers);

            var vsyncResult = reconciliationBacenPort.syncVerification(vsyncCurrent);

            var vsyncHistoric = syncVerifier.syncVerificationResult(vsyncCurrent, vsyncResult);

            syncVerifierPort.update(syncVerifier);
            syncVerifierHistoricPort.save(vsyncHistoric);

            if (syncVerifier.isNOk()) {
                failureReconciliationMessagePort.send(vsyncHistoric);
            }

            log.info("ReconciliationSync_keyType_result {} {} {} {}",
                kv("startCurrentTimeMillis", startCurrentTimeMillis),
                kv("keyType", vsyncHistoric.getKeyType()),
                kv("synchronizedAt", vsyncHistoric.getSynchronizedStart()),
                kv("vsyncResult", vsyncHistoric.getSyncVerifierResult()));
        });

        log.info("ReconciliationSync_done {} {}",
            kv("startCurrentTimeMillis", startCurrentTimeMillis),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / 1000));
    }

}
