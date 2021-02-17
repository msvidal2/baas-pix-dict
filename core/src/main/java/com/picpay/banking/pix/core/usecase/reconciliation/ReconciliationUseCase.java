/*
 *  baas-pix-dict 1.0 17/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * @author rafael.braga
 * @version 1.0 17/02/2021
 */
@RequiredArgsConstructor
@Slf4j
public class ReconciliationUseCase {

    private final FailureReconciliationSyncByFileUseCase syncByFileUseCase;
    private final ReconciliationSyncUseCase reconciliationSyncUseCase;
    private final FailureReconciliationSyncUseCase failureReconciliationSyncUseCase;
    private final SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase;
    private final ReconciliationLockPort reconciliationLockPort;

    public SyncVerifierHistoric execute(KeyType syncKeyType) {
        try {
            reconciliationLockPort.lock();
            return run(syncKeyType);
        } finally {
            reconciliationLockPort.unlock();
        }
    }

    private SyncVerifierHistoric run(final KeyType syncKeyType) {
        log.info("Inicio da sync por: {}", kv("keyType", syncKeyType.name()));
        sincronizeCIDEventsUseCase.syncByKeyType(syncKeyType);

        SyncVerifierHistoric syncVerifierHistoric = reconciliationSyncUseCase.execute(syncKeyType);

        if (syncVerifierHistoric.isNOK()) {
            SyncVerifierHistoric executedHistoric = failureReconciliationSyncUseCase.execute(syncVerifierHistoric);

            if (executedHistoric.isNOK()) {
                log.warn("Sync por eventos: NOK. Iniciando tratamento por arquivos. KeyType: {}", syncKeyType);
                syncByFileUseCase.execute(syncKeyType);
            }
        }

        log.info("Termino da sync: {}, {}", kv("keyType", syncKeyType.name()), kv("syncVerifierHistoric", syncVerifierHistoric));
        return syncVerifierHistoric;
    }

}
