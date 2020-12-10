package com.picpay.banking.pix.sync.config;

import com.picpay.banking.pix.core.ports.reconciliation.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.usecase.reconciliation.ContentIdentifierEventRecordUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.services.ContentIdentifierEventRecordService;
import com.picpay.banking.pix.core.usecase.reconciliation.services.ContentIdentifierEventValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SyncBeanConfig {

    @Bean
    public static ContentIdentifierEventRecordUseCase cidProviderUseCase(final ContentIdentifierEventRecordService cidEventRegisterService,
                                                                         final ContentIdentifierEventValidator cidValidator) {
        return new ContentIdentifierEventRecordUseCase(cidEventRegisterService, cidValidator);
    }

    @Bean
    public static ContentIdentifierEventRecordService cidEventRegisterService(final ContentIdentifierEventPort cidRegisterPort) {
        return new ContentIdentifierEventRecordService(cidRegisterPort);
    }

    @Bean
    public static ContentIdentifierEventValidator cidValidator(final ContentIdentifierEventPort contentIdentifierPort) {
        return new ContentIdentifierEventValidator(contentIdentifierPort);
    }
}
