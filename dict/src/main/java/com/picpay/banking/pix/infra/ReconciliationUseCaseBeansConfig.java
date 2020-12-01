package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.FailureReconciliationMessagePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateAccountPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.CidProviderUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationSyncUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.RequestSyncFileUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReconciliationUseCaseBeansConfig {

    @Bean
    public CidProviderUseCase cidProviderUseCase() {
        return new CidProviderUseCase();
    }

    @Bean
    public FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase(
        @Value("${picpay.ispb}") Integer participant,
        BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
        DatabaseContentIdentifierPort databaseContentIdentifierPort, BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort,
        CreatePixKeyPort createPixKeyPort, RemovePixKeyPort removePixKeyPort, FindPixKeyPort findPixKeyPort) {
        return new FailureReconciliationSyncByFileUseCase(bacenContentIdentifierEventsPort, databaseContentIdentifierPort,
            bacenPixKeyByContentIdentifierPort, createPixKeyPort, findPixKeyPort, removePixKeyPort, participant);
    }

    @Bean
    public FailureReconciliationSyncUseCase failureReconciliationSyncUseCase(BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
        BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort, ContentIdentifierActionPort contentIdentifierActionPort,
        ContentIdentifierEventPort contentIdentifierEventPort, CreatePixKeyUseCase createPixKeyUseCase,
        UpdateAccountPixKeyUseCase updateAccountPixKeyUseCase, RemovePixKeyUseCase removePixKeyUseCase,
        FindPixKeyPort findPixKeyPort) {

        return new FailureReconciliationSyncUseCase(bacenContentIdentifierEventsPort, bacenPixKeyByContentIdentifierPort,
            contentIdentifierActionPort, contentIdentifierEventPort, createPixKeyUseCase, updateAccountPixKeyUseCase,
            removePixKeyUseCase, findPixKeyPort);
    }

    @Bean
    public ReconciliationSyncUseCase reconciliationSyncUseCase(SyncVerifierPort syncVerifierPort, SyncVerifierHistoricPort syncVerifierHistoricPort,
        ContentIdentifierEventPort contentIdentifierEventPort, BacenSyncVerificationsPort bacenSyncVerificationsPort,
        FailureReconciliationMessagePort failureReconciliationMessagePort) {
        return new ReconciliationSyncUseCase(syncVerifierPort, syncVerifierHistoricPort, contentIdentifierEventPort, bacenSyncVerificationsPort,
            failureReconciliationMessagePort);
    }

    @Bean
    public RequestSyncFileUseCase requestSyncFileUseCase(BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
        DatabaseContentIdentifierPort databaseContentIdentifierPort) {
        return new RequestSyncFileUseCase(bacenContentIdentifierEventsPort, databaseContentIdentifierPort);
    }

}
