package com.picpay.banking.reconciliation.config;

import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.usecase.reconciliation.ContentIdentifierEventRecordUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ReconciliationConfig {

    private final ContentIdentifierEventPort contentIdentifierEventPort;

    @Bean
    public ContentIdentifierEventRecordUseCase contentIdentifierEventRecordUseCase() {
        return new ContentIdentifierEventRecordUseCase(contentIdentifierEventPort);
    }

}
