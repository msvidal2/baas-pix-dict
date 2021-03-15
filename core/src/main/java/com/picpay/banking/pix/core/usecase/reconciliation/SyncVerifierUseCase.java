package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@AllArgsConstructor
public class SyncVerifierUseCase {

    private final SyncVerifierPort syncVerifierPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final ContentIdentifierPort contentIdentifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;

    public SyncVerifierHistoric execute(KeyType keyType) {
        var startCurrentTimeMillis = System.currentTimeMillis();
        log.info("ReconciliationSync_started {}", kv("keyType", keyType));

        var syncVerifier = syncVerifierPort.getLastSuccessfulVsync(keyType);

        List<String> cids = contentIdentifierPort.findAllCidsAfterLastSuccessfulVsync(
            syncVerifier.getKeyType(),
            syncVerifier.getSynchronizedAt());
        var vsyncCurrent = syncVerifier.calculateVsync(cids.stream());

        var syncVerifierResult = bacenSyncVerificationsPort.syncVerification(keyType, vsyncCurrent);
        var vsyncHistoric = syncVerifier.syncVerificationResult(vsyncCurrent, syncVerifierResult);

        syncVerifierPort.save(syncVerifier);
        vsyncHistoric = syncVerifierHistoricPort.save(vsyncHistoric);

        final int MILLISECONDS_TO_SECONDS = 1000;
        log.info("ReconciliationSync_ended {}, {}, {}",
            kv("keyType", keyType),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / MILLISECONDS_TO_SECONDS),
            kv("syncVerifierHistoric", vsyncHistoric));

        return vsyncHistoric;
    }

}
