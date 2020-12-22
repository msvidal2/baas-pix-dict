package com.picpay.banking.claim.config;

import com.picpay.banking.claim.ports.SendClaimNotificationPortImpl;
import com.picpay.banking.pix.core.ports.claim.bacen.AcknowledgeClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
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
                                                                   SendClaimNotificationPortImpl sendClaimNotificationPortImpl) {
        return new PollingClaimListenerUseCase(
                participant,
                acknowledgeClaimPort,
                saveClaimPort,
                sendClaimNotificationPortImpl);
    }

    @Bean
    public OverduePossessionClaimUseCase overduePossessionClaimUseCase(ConfirmClaimPort confirmClaimPort,
                                                                       CreateClaimPort saveClaimPort,
                                                                       RemovePixKeyAutomaticallyPort removePixKeyAutomaticallyPort) {
        return new OverduePossessionClaimUseCase(confirmClaimPort, saveClaimPort, removePixKeyAutomaticallyPort);
    }

}
