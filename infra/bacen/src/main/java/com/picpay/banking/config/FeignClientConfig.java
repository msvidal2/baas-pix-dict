package com.picpay.banking.config;

import com.picpay.banking.reconciliation.clients.BacenArqClient;
import feign.Feign;
import feign.Target;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
@Configuration
@Import(FeignClientsConfiguration.class)
@EnableFeignClients(basePackages = {
    "com.picpay.banking.pixkey.clients",
    "com.picpay.banking.claim.clients",
    "com.picpay.banking.infraction.clients",
    "com.picpay.banking.reconciliation.clients",

})
public class FeignClientConfig {

    @Bean
    public BacenArqClient bacenArqClient() {
        return Feign.builder()
            .target(Target.EmptyTarget.create(BacenArqClient.class));
    }

}
