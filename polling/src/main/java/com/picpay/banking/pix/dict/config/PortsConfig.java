package com.picpay.banking.pix.dict.config;

import com.picpay.banking.claim.ports.picpay.SendToProcessClaimNotificationPortImpl;
import com.picpay.banking.config.ClaimTopicBinding;
import com.picpay.banking.pix.core.ports.claim.SendToProcessClaimNotificationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortsConfig {

    @Bean
    public SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort(ClaimTopicBinding claimTopicBinding) {
        return new SendToProcessClaimNotificationPortImpl(claimTopicBinding);
    }

}
