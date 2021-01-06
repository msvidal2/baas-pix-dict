package com.picpay.banking.pix.sync.file.task;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

/**
 * @author Luis Silva
 * @version 1.0 10/12/2020
 */
@Component
public class SyncFileTask implements ApplicationRunner {

    private FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase;

    public SyncFileTask(final FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase) {
        this.failureReconciliationSyncByFileUseCase = failureReconciliationSyncByFileUseCase;
    }

    @Override
    @Transactional
    public void run(final ApplicationArguments args) throws Exception {
        Arrays.stream(KeyType.values()).forEach(keyType -> {
            this.failureReconciliationSyncByFileUseCase.execute(keyType);
        });
    }

}
