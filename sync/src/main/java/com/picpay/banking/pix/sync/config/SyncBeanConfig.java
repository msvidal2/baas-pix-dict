package com.picpay.banking.pix.sync.config;

import com.picpay.banking.pix.core.usecase.reconciliation.CidProviderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SyncBeanConfig {

    @Bean
    public static CidProviderUseCase cidProviderUseCase() { return new CidProviderUseCase(); }
}
