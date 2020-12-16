package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifier;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
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
    private final ContentIdentifierPort contentIdentifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;

    public SyncVerifierHistoric execute(KeyType keyType) {
        var startCurrentTimeMillis = System.currentTimeMillis();
        log.info("ReconciliationSync_started {} {}",
            kv("keyType", keyType),
            kv("startCurrentTimeMillis", startCurrentTimeMillis));

        var syncVerifier = syncVerifierPort.getLastSuccessfulVsync(keyType)
            .orElseGet(() -> SyncVerifier.builder()
                .keyType(keyType)
                .synchronizedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .build());

        List<String> cids = contentIdentifierPort.findAllCidsAfterLastSuccessfulVsync(
            syncVerifier.getKeyType(),
            syncVerifier.getSynchronizedAt());

        var vsyncCurrent = syncVerifier.calculateVsync(cids);
        var syncVerifierResult = bacenSyncVerificationsPort.syncVerification(keyType, vsyncCurrent);
        var vsyncHistoric = syncVerifier.syncVerificationResult(vsyncCurrent, syncVerifierResult);

        syncVerifierPort.save(syncVerifier);
        vsyncHistoric = syncVerifierHistoricPort.save(vsyncHistoric);

        log.info("ReconciliationSync_done {} {} {}",
            kv("keyType", keyType),
            kv("startCurrentTimeMillis", startCurrentTimeMillis),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / 1000));

        return vsyncHistoric;
    }

}
