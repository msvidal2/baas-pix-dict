package com.picpay.banking.pix.sync.file.task;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Luis Silva
 * @version 1.0 10/12/2020
 */
@Component
public class SyncFileTask implements ApplicationRunner {

    private FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase;
    private KeyType keyType;

    public SyncFileTask(final FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase, @Value("${sync.key-type}") final KeyType keyType) {
        this.failureReconciliationSyncByFileUseCase = failureReconciliationSyncByFileUseCase;
        this.keyType = keyType;
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        this.failureReconciliationSyncByFileUseCase.execute(keyType);
    }

}
