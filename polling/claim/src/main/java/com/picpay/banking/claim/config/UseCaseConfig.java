package com.picpay.banking.claim.config;

import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimLastPollingDatePort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.claim.picpay.UpdateClaimLastPollingDatePort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import com.picpay.banking.pix.core.usecase.claim.PollingClaimUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public PollingClaimUseCase pollingClaimUseCase(final ListClaimsBacenPort listClaimsBacenPort,
                                                   final SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort,
                                                   final FindClaimLastPollingDatePort findClaimLastPollingDatePort,
                                                   final UpdateClaimLastPollingDatePort updateClaimLastPollingDatePort) {
        return new PollingClaimUseCase(listClaimsBacenPort, sendToProcessClaimNotificationPort, findClaimLastPollingDatePort, updateClaimLastPollingDatePort);

    }

}
