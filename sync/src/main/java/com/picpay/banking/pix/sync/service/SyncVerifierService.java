package com.picpay.banking.pix.sync.service;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationSyncUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SyncVerifierService {

    private final ReconciliationSyncUseCase reconciliationSyncUseCase;
    private final FailureReconciliationSyncUseCase failureReconciliationSyncUseCase;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SyncVerifierHistoric syncVerifier(final KeyType keyType) {
        return reconciliationSyncUseCase.execute(keyType);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void failureReconciliationSync(final SyncVerifierHistoric syncVerifierHistoric) {
        failureReconciliationSyncUseCase.execute(syncVerifierHistoric);
    }

}
