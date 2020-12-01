package com.picpay.banking.pix.infra;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.interceptors.FeignClientInterceptor;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.jdpi.ports.claim.ClaimCancelPortImpl;
import com.picpay.banking.jdpi.ports.claim.ClaimConfirmationPortImpl;
import com.picpay.banking.jdpi.ports.claim.CompleteClaimPortImpl;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmationClaimPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "pix.partner", havingValue = "bacen")
public class BacenPortBeansConfig {

    @Bean
    public FeignClientInterceptor feignClientInterceptor(TokenManagerClient tokenManagerClient,
                                                         TimeLimiterExecutor timeLimiterExecutor) {
        return new FeignClientInterceptor(tokenManagerClient, timeLimiterExecutor);
    }

    @Bean
    public CancelClaimPort claimCancelPort(ClaimJDClient claimJDClient, TimeLimiterExecutor timeLimiterExecutor) {
        return new ClaimCancelPortImpl(claimJDClient, timeLimiterExecutor);
    }

    @Bean
    public ConfirmationClaimPort claimConfirmationPort(ClaimJDClient claimJDClient,
                                                       TimeLimiterExecutor timeLimiterExecutor) {
        return new ClaimConfirmationPortImpl(claimJDClient, timeLimiterExecutor);
    }

    @Bean
    public CompleteClaimPort completeClaimPort(ClaimJDClient claimJDClient, TimeLimiterExecutor timeLimiterExecutor) {
        return new CompleteClaimPortImpl(claimJDClient, timeLimiterExecutor);
    }


}
