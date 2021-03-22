/*
 *  baas-pix-dict 1.0 18/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.infra.config;

import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.infraction.bacen.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.*;
import com.picpay.banking.pixkey.config.DictEventOutputBinding;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 18/03/2021
 */
@Configuration
@EnableBinding(value = {StreamConfig.class, DictEventOutputBinding.class})
@RequiredArgsConstructor
public class UseCaseConfig {

    private final PixKeyEventRegistryPort eventRegistryPort;

    @Bean
    public CreateInfractionReportUseCase infractionReportCreateBacenProcessor(final CreateInfractionReportPort infractionReportPort,
        final InfractionReportFindPort infractionReportFindPort,
        @Value("${picpay.ispb}") final String ispbPicPay) {
        return new CreateInfractionReportUseCase(infractionReportPort, infractionReportFindPort, ispbPicPay);
    }

    @Bean
    public PixKeyEventRegistryUseCase pixKeyEventRegistryUseCase() {
        return new PixKeyEventRegistryUseCase(eventRegistryPort);
    }

    @Bean
    public CreatePixKeyBacenUseCase createPixKeyBacenUseCase(final CreatePixKeyBacenPort createPixKeyBacenPortBacen,
            final FindPixKeyPort findPixKeyPort,
            final FindOpenClaimByKeyPort findOpenClaimByKeyPort) {

        return new CreatePixKeyBacenUseCase(createPixKeyBacenPortBacen, findPixKeyPort, findOpenClaimByKeyPort);
    }

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
    public UpdateBacenPixKeyUseCase updateBacenPixKeyUseCase(UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort,
                                                             FindPixKeyPort findPixKeyPort){
        return new UpdateBacenPixKeyUseCase(updateAccountPixKeyBacenPort, findPixKeyPort);
    }

    @Bean
    public RemoveBacenPixKeyUseCase removeBacenPixKeyUseCase(RemovePixKeyBacenPort removePixKeyBacenPort){
        return new RemoveBacenPixKeyUseCase(removePixKeyBacenPort);
    }

}
