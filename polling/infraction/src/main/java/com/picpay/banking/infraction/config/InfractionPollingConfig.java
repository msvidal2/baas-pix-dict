/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.config;

import com.picpay.banking.pix.core.ports.infraction.bacen.ListInfractionPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.SendToAcknowledgePort;
import com.picpay.banking.pix.core.usecase.infraction.InfractionPollingUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@Configuration
public class InfractionPollingConfig {

    @Bean
    public InfractionPollingUseCase infractionPollingUseCase(final SendToAcknowledgePort senderPort,
                                                             final ListInfractionPort bacenClient,
                                                             final ExecutionPort executionPort) {
        return new InfractionPollingUseCase(senderPort, bacenClient, executionPort);
    }

}
