package com.picpay.banking.reconciliation.config;

import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncBacenCidEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.usecase.reconciliation.SincronizeCIDEventsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SincronizeCIDEventsUseCaseConfig {

    private final SyncBacenCidEventsPort syncBacenCidEventsPort;
    private final SyncVerifierPort syncVerifierPort;
    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;

    @Bean
    public SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase() {
        return new SincronizeCIDEventsUseCase(syncBacenCidEventsPort, syncVerifierPort, bacenContentIdentifierEventsPort);
    }

}
