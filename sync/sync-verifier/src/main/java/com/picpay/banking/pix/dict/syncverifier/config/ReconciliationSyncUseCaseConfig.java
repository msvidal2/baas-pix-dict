package com.picpay.banking.pix.dict.syncverifier.config;

import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationSyncUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ReconciliationSyncUseCaseConfig {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final SyncVerifierHistoricActionPort syncVerifierHistoricActionPort;
    private final ContentIdentifierEventPort contentIdentifierEventPort;
    private final CreatePixKeyPort createPixKeyPort;
    private final UpdateAccountPixKeyPort updateAccountPixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private final SyncVerifierPort syncVerifierPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final ContentIdentifierPort contentIdentifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;

    @Bean
    public ReconciliationSyncUseCase reconciliationSyncUseCase() {
        return new ReconciliationSyncUseCase(syncVerifierPort, syncVerifierHistoricPort, contentIdentifierPort,
            bacenSyncVerificationsPort);
    }

    @Bean
    public FailureReconciliationSyncUseCase failureReconciliationSyncUseCase() {
        return new FailureReconciliationSyncUseCase(bacenContentIdentifierEventsPort, bacenPixKeyByContentIdentifierPort,
            syncVerifierHistoricActionPort,
            contentIdentifierEventPort, createPixKeyPort, updateAccountPixKeyPort, removePixKeyPort, findPixKeyPort);
    }

}