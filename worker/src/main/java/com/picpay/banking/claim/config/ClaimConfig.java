package com.picpay.banking.claim.config;

import com.picpay.banking.claim.ports.SendClaimNotificationPortImpl;
import com.picpay.banking.pix.core.ports.claim.bacen.AcknowledgeClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
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

}
