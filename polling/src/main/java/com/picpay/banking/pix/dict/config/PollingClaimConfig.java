package com.picpay.banking.pix.dict.config;

import com.picpay.banking.pix.core.ports.claim.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import com.picpay.banking.pix.core.usecase.claim.PollingClaimsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PollingClaimConfig {

    @Bean
    public PollingClaimsUseCase pullingClaimUseCase(final ListClaimsBacenPort listClaimsBacenPort,
                                                    final SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort) {
        return new PollingClaimsUseCase(listClaimsBacenPort, sendToProcessClaimNotificationPort);

    }

}
