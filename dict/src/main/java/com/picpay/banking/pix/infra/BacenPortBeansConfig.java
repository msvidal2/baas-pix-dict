package com.picpay.banking.pix.infra;

import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.interceptors.FeignClientInterceptor;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
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

}
