/*
 *  baas-pix-dict 1.0 18/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.infra.config;

import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreateDatabasePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.PixKeyEventRegistryUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemoveDatabasePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateDatabasePixKeyUseCase;
import com.picpay.banking.pixkey.config.DictEventOutputBinding;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(value = {DomainEventsStreamConfig.class, DictEventOutputBinding.class})
@RequiredArgsConstructor
public class PixKeyUseCaseConfig {

    @Bean
    public CreateDatabasePixKeyUseCase createDatabasePixKeyUseCase(SavePixKeyPort savePixKeyPort,
        ContentIdentifierEventPort contentIdentifierEventPort) {
        return new CreateDatabasePixKeyUseCase(savePixKeyPort, contentIdentifierEventPort);
    }

    @Bean
    public UpdateDatabasePixKeyUseCase updateDatabasePixKeyUseCase(SavePixKeyPort savePixKeyPort,
        FindPixKeyPort findPixKeyPort,
        ContentIdentifierEventPort contentIdentifierEventPort) {
        return new UpdateDatabasePixKeyUseCase(savePixKeyPort, findPixKeyPort, contentIdentifierEventPort);
    }

    @Bean
    public RemoveDatabasePixKeyUseCase removeDatabasePixKeyUseCase(
        RemovePixKeyPort removePixKeyPort,
        ContentIdentifierEventPort contentIdentifierEventPort) {
        return new RemoveDatabasePixKeyUseCase(removePixKeyPort, contentIdentifierEventPort);
    }

    @Bean
    public PixKeyEventRegistryUseCase pixKeyEventRegistryUseCase(PixKeyEventRegistryPort eventRegistryPort) {
        return new PixKeyEventRegistryUseCase(eventRegistryPort);
    }

}
