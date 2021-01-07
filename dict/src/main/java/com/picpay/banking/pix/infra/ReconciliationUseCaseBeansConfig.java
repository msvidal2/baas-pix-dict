package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.RequestSyncFileUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReconciliationUseCaseBeansConfig {

    @Bean
    public FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase(
            @Value("${picpay.ispb}") Integer participant,
            BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
            DatabaseContentIdentifierPort databaseContentIdentifierPort, BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort,
            SavePixKeyPort savePixKeyPort, RemovePixKeyPort removePixKeyPort, FindPixKeyPort findPixKeyPort) {
        return new FailureReconciliationSyncByFileUseCase(participant, bacenContentIdentifierEventsPort, databaseContentIdentifierPort,
            bacenPixKeyByContentIdentifierPort, savePixKeyPort, findPixKeyPort, removePixKeyPort);
    }

    @Bean
    public RequestSyncFileUseCase requestSyncFileUseCase(BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
        DatabaseContentIdentifierPort databaseContentIdentifierPort) {
        return new RequestSyncFileUseCase(bacenContentIdentifierEventsPort, databaseContentIdentifierPort);
    }

}
