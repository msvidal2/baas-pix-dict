package com.picpay.banking.mockserver.config;

import org.mockserver.integration.ClientAndServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class MockServerConfig {

    @Bean
    @Scope("singleton")
    public ClientAndServer clientAndServer() {
        return new ClientAndServer(1080);
    }

}
