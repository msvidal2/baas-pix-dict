/*
 *  baas-pix-dict 1.0 18/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.infra.config;

import com.picpay.banking.pix.core.ports.infraction.bacen.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.bacen.InfractionReportAnalyzePort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.PixKeyEventRegistryUseCase;
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
    public AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase(final InfractionReportAnalyzePort infractionReportAnalyzePort,
                                                                         final InfractionReportFindPort infractionReportFindPort,
                                                                         @Value("${picpay.ispb}") final String ispbPicPay) {
        return new AnalyzeInfractionReportUseCase(infractionReportAnalyzePort, infractionReportFindPort, ispbPicPay);
    }

    @Bean
    public PixKeyEventRegistryUseCase pixKeyEventRegistryUseCase() {
        return new PixKeyEventRegistryUseCase(eventRegistryPort);
    }

}
