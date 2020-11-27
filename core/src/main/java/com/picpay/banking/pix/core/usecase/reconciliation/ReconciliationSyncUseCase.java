package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Vsync;
import com.picpay.banking.pix.core.ports.reconciliation.BacenReconciliationPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseReconciliationPort;
import com.picpay.banking.pix.core.ports.reconciliation.FailureMessagePort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@AllArgsConstructor
public class ReconciliationSyncUseCase {

    private final DatabaseReconciliationPort databaseReconciliationPort;
    private final BacenReconciliationPort bacenReconciliationPort;
    private final FailureMessagePort failureMessagePort;

    public void execute() {
        var startCurrentTimeMillis = System.currentTimeMillis();
        log.info("ReconciliationSync_started", kv("startCurrentTimeMillis", startCurrentTimeMillis));

        List.of(KeyType.values()).forEach(keyType -> {
            var vsync = databaseReconciliationPort.getLastSuccessfulVsync(keyType)
                .orElseGet(() -> Vsync.builder()
                    .keyType(keyType)
                    .synchronizedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .build());

            List<ContentIdentifier> contentIdentifiers = databaseReconciliationPort.listAfterLastSuccessfulVsync(vsync.getKeyType(),
                vsync.getSynchronizedAt());

            vsync.calculateVsync(contentIdentifiers);

            var result = bacenReconciliationPort.syncVerification(vsync.getVsync());

            vsync.syncVerificationResult(result);

            databaseReconciliationPort.updateVerificationResult(vsync);

            if (vsync.isNOk()) {
                failureMessagePort.sendMessageForSincronization(vsync);
            }

            log.info("ReconciliationSync_keytype_result",
                kv("startCurrentTimeMillis", startCurrentTimeMillis),
                kv("keyType", vsync.getKeyType()),
                kv("synchronizedAt", vsync.getSynchronizedAt()),
                kv("vsyncResult", vsync.getVsyncResult()));
        });

        log.info("ReconciliationSync_done", kv("startCurrentTimeMillis", startCurrentTimeMillis));
    }

}
