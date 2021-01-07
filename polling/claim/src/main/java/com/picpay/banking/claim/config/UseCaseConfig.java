package com.picpay.banking.claim.config;

import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import com.picpay.banking.pix.core.usecase.claim.PollingOverduePortabilityUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public PollingOverduePortabilityUseCase pollingClaimUseCase(final ListClaimsBacenPort listClaimsBacenPort,
                                                                final SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort,
                                                                final ExecutionPort executionPort) {
        return new PollingOverduePortabilityUseCase(listClaimsBacenPort, sendToProcessClaimNotificationPort, executionPort);
    }

}
