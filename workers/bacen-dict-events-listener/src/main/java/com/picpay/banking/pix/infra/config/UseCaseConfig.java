/*
 *  baas-pix-dict 1.0 18/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.infra.config;

import com.picpay.banking.pix.core.ports.infraction.bacen.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionEventRegistryPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 18/03/2021
 */
@Configuration
public class UseCaseConfig {

    @Bean
    public CreateInfractionReportUseCase infractionReportCreateBacenProcessor(final CreateInfractionReportPort infractionReportPort,
                                                                              final InfractionEventRegistryPort infractionEventRegistryPort,
                                                                              final InfractionReportFindPort infractionReportFindPort,
                                                                              @Value("${picpay.ispb}") final String ispbPicPay) {
        return new CreateInfractionReportUseCase(infractionReportPort, infractionEventRegistryPort, infractionReportFindPort, ispbPicPay);
    }

}
