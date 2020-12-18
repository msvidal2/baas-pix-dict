package com.picpay.banking.claim.config;

import com.picpay.banking.claim.ports.SendClaimNotificationPortImpl;
import com.picpay.banking.pix.core.ports.claim.bacen.AcknowledgeClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToCancelPortabilityPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import com.picpay.banking.pix.core.usecase.claim.CancelPortabilityPollingUseCase;
import com.picpay.banking.pix.core.usecase.claim.PollingClaimListenerUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClaimConfig {

    @Bean
    public PollingClaimListenerUseCase pollingClaimListenerUseCase(@Value("${picpay.ispb}") Integer participant,
                                                                   AcknowledgeClaimPort acknowledgeClaimPort,
                                                                   CreateClaimPort saveClaimPort,
                                                                   SendClaimNotificationPortImpl sendClaimNotificationPortImpl) {
        return new PollingClaimListenerUseCase(
                participant,
                acknowledgeClaimPort,
                saveClaimPort,
                sendClaimNotificationPortImpl);
    }

    @Bean
    public CancelPortabilityPollingUseCase cancelPortabilityPollingUseCase(FindClaimToCancelPort findClaimToCancelPort,
                                                                           CancelClaimBacenPort cancelClaimBacenPort,
                                                                           CancelClaimPort cancelClaimPort,
                                                                           ExecutionPort executionPort,
                                                                           SendToCancelPortabilityPort sendToCancelPortabilityPort) {
        return new CancelPortabilityPollingUseCase(
                findClaimToCancelPort,
                cancelClaimBacenPort,
                cancelClaimPort,
                executionPort,
                sendToCancelPortabilityPort);
    }

}
