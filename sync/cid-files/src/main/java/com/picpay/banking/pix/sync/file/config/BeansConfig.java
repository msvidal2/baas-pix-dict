package com.picpay.banking.pix.sync.file.config;

import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Luis Silva
 * @version 1.0 10/12/2020
 */
@Configuration
public class BeansConfig {

    @Bean
    public FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase(
        @Value("${picpay.ispb}") Integer participant,
        BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
        DatabaseContentIdentifierPort databaseContentIdentifierPort, BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort,
        CreatePixKeyPort createPixKeyPort, RemovePixKeyPort removePixKeyPort, FindPixKeyPort findPixKeyPort) {
        return new FailureReconciliationSyncByFileUseCase(participant,bacenContentIdentifierEventsPort, databaseContentIdentifierPort,
            bacenPixKeyByContentIdentifierPort, createPixKeyPort, findPixKeyPort,removePixKeyPort);
    }

}
