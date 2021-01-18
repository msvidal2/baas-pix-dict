package com.picpay.banking.pix.sync.file.task;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SyncFileTask implements ApplicationRunner {

    private final FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase;
    private final ReconciliationLockPort lockPort;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) throws Exception {
        try {
            lockPort.lock();
            Arrays.stream(KeyType.values()).forEach(this.failureReconciliationSyncByFileUseCase::execute);
        } finally {
            lockPort.unlock();
        }
    }

}
