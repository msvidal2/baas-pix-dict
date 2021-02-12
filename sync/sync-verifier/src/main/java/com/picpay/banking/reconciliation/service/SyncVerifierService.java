package com.picpay.banking.reconciliation.service;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationSyncUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncVerifierService {

    private final ReconciliationSyncUseCase reconciliationSyncUseCase;
    private final FailureReconciliationSyncUseCase failureReconciliationSyncUseCase;

    @Trace(dispatcher = true, metricName = "syncVerifier")
    public SyncVerifierHistoric syncVerifier(final KeyType keyType) {
        return reconciliationSyncUseCase.execute(keyType);
    }

    @Trace(dispatcher = true, metricName = "failureReconciliationSync")
    public SyncVerifierHistoric failureReconciliationSync(final SyncVerifierHistoric syncVerifierHistoric) {
        return failureReconciliationSyncUseCase.execute(syncVerifierHistoric);
    }

}
