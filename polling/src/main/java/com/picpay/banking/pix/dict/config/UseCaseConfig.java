package com.picpay.banking.pix.dict.config;

import com.picpay.banking.claim.ports.picpay.SendToProcessClaimNotificationPortImpl;
import com.picpay.banking.config.ClaimTopicBinding;
import com.picpay.banking.pix.core.ports.claim.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import com.picpay.banking.pix.core.usecase.claim.PollingClaimUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort(ClaimTopicBinding claimTopicBinding) {
        return new SendToProcessClaimNotificationPortImpl(claimTopicBinding);
    }

    @Bean
    public PollingClaimUseCase pollingClaimUseCase(final ListClaimsBacenPort listClaimsBacenPort,
                                                   final SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort) {
        return new PollingClaimUseCase(listClaimsBacenPort, sendToProcessClaimNotificationPort);

    }

}
