package com.picpay.banking.claim.config;

import com.picpay.banking.pix.core.ports.claim.bacen.AcknowledgeClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendClaimNotificationPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToCancelPortabilityPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import com.picpay.banking.pix.core.usecase.claim.OverduePortabilityClaimUseCase;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyAutomaticallyPort;
import com.picpay.banking.pix.core.usecase.claim.OverduePossessionClaimUseCase;
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
                                                                   SendClaimNotificationPort sendClaimNotificationPort) {
        return new PollingClaimListenerUseCase(
                participant,
                acknowledgeClaimPort,
                saveClaimPort,
                sendClaimNotificationPort);
    }

    @Bean
    public OverduePortabilityClaimUseCase cancelPortabilityPollingUseCase(FindClaimToCancelPort findClaimToCancelPort,
                                                                          CancelClaimBacenPort cancelClaimBacenPort,
                                                                          CancelClaimPort cancelClaimPort,
                                                                          ExecutionPort executionPort,
                                                                          SendToCancelPortabilityPort sendToCancelPortabilityPort) {
        return new OverduePortabilityClaimUseCase(
                findClaimToCancelPort,
                cancelClaimBacenPort,
                cancelClaimPort,
                executionPort,
                sendToCancelPortabilityPort);
    }

    @Bean
    public OverduePossessionClaimUseCase overduePossessionClaimUseCase(ConfirmClaimPort confirmClaimPort,
                                                                       CreateClaimPort saveClaimPort,
                                                                       RemovePixKeyAutomaticallyPort removePixKeyAutomaticallyPort,
                                                                       CancelClaimPort cancelClaimPort,
                                                                       CancelClaimBacenPort cancelClaimBacenPort) {
        return new OverduePossessionClaimUseCase(confirmClaimPort, saveClaimPort, removePixKeyAutomaticallyPort,
                cancelClaimPort, cancelClaimBacenPort);
    }

}
