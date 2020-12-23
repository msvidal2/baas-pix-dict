package com.picpay.banking.pix.sync.file.task;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Luis Silva
 * @version 1.0 10/12/2020
 */
@Component
@RequiredArgsConstructor
public class SyncFileTask implements ApplicationRunner {

    private final FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        Arrays.stream(KeyType.values()).forEach(this.failureReconciliationSyncByFileUseCase::execute);
    }

}
