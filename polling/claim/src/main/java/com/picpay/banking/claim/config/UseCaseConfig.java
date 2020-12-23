package com.picpay.banking.claim.config;

import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendOverduePossessionClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import com.picpay.banking.pix.core.usecase.claim.CancelPortabilityPollingUseCase;
import com.picpay.banking.pix.core.usecase.claim.PollingClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.PollingOverduePossessionClaimUseCase;
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
                                                                           final ExecutionPort executionPort,
                                                                           final SendToCancelPortabilityPort sendToCancelPortabilityPort) {
        return new CancelPortabilityPollingUseCase(findClaimToCancelPort, cancelClaimBacenPort, cancelClaimPort, executionPort, sendToCancelPortabilityPort);
    }

    @Bean
    public PollingOverduePossessionClaimUseCase pollingOverduePossessionClaimUseCase(
            final FindClaimToCancelPort findClaimToCancelPort,
            final SendOverduePossessionClaimPort sendOverduePossessionClaimPort) {
        return new PollingOverduePossessionClaimUseCase(findClaimToCancelPort, sendOverduePossessionClaimPort);
    }

}
