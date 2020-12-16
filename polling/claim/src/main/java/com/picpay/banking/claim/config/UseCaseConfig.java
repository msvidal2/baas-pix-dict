package com.picpay.banking.claim.config;

import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import com.picpay.banking.pix.core.usecase.claim.CancelPortabilityPollingUseCase;
import com.picpay.banking.pix.core.usecase.claim.PollingClaimUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public PollingClaimUseCase pollingClaimUseCase(final ListClaimsBacenPort listClaimsBacenPort,
                                                   final SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort,
                                                   final ExecutionPort executionPort) {
        return new PollingClaimUseCase(listClaimsBacenPort, sendToProcessClaimNotificationPort, executionPort);
    }

    @Bean
    public CancelPortabilityPollingUseCase cancelPortabilityPollingUseCase(final FindClaimToCancelPort findClaimToCancelPort,
                                                                           final CancelClaimBacenPort cancelClaimBacenPort,
                                                                           final CancelClaimPort cancelClaimPort,
                                                                           final ExecutionPort executionPort) {
        return new CancelPortabilityPollingUseCase(findClaimToCancelPort, cancelClaimBacenPort, cancelClaimPort, executionPort);
    }

}
