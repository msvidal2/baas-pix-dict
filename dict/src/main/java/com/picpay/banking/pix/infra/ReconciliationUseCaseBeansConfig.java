package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseVsyncPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
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
        return new FailureReconciliationSyncByFileUseCase(participant,bacenContentIdentifierEventsPort, databaseContentIdentifierPort,
            bacenPixKeyByContentIdentifierPort, createPixKeyPort, findPixKeyPort,removePixKeyPort);
    }

    @Bean
    public FailureReconciliationSyncUseCase failureReconciliationSyncUseCase(BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
        BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort, CreatePixKeyUseCase createPixKeyUseCase,
        RemovePixKeyUseCase removePixKeyUseCase) {
        return new FailureReconciliationSyncUseCase(bacenContentIdentifierEventsPort, bacenPixKeyByContentIdentifierPort, createPixKeyUseCase,
            removePixKeyUseCase);
    }

    @Bean
    public ReconciliationSyncUseCase reconciliationSyncUseCase(DatabaseVsyncPort databaseVsyncPort, DatabaseContentIdentifierPort databaseContentIdentifierPort,
        BacenSyncVerificationsPort bacenSyncVerificationsPort, FailureReconciliationSyncUseCase failureReconciliationSyncUseCase) {
        return new ReconciliationSyncUseCase(databaseVsyncPort, databaseContentIdentifierPort, bacenSyncVerificationsPort,
            failureReconciliationSyncUseCase);
    }

    @Bean
    public RequestSyncFileUseCase requestSyncFileUseCase(BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort ,DatabaseContentIdentifierPort databaseContentIdentifierPort) {
        return new RequestSyncFileUseCase(bacenContentIdentifierEventsPort, databaseContentIdentifierPort);
    }

}
