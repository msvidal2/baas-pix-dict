package com.picpay.banking.pix.sync.schedulers;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.sync.service.SyncVerifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncVerifierScheduler {

    private final SyncVerifierService syncVerifierService;

    public void run() {
        var keyType = KeyType.RANDOM;

        SyncVerifierHistoric syncVerifierHistoric = syncVerifierService.syncVerifier(keyType);
        if (syncVerifierHistoric.isNOK()) {
            syncVerifierService.failureReconciliationSync(syncVerifierHistoric);
            syncVerifierHistoric = syncVerifierService.syncVerifier(keyType);

            log.info("SyncVerifierScheduler {}", syncVerifierHistoric);
        }
    }

}
