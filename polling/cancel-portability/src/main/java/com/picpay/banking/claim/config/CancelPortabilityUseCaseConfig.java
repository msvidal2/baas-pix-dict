/*
 *  baas-pix-dict 1.0 22/12/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.claim.config;

import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToCancelPortabilityPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import com.picpay.banking.pix.core.usecase.claim.OverduePortabilityClaimUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 22/12/2020
 */
@Configuration
public class CancelPortabilityUseCaseConfig {

    @Bean
    public OverduePortabilityClaimUseCase cancelPortabilityPollingUseCase(final FindClaimToCancelPort findClaimToCancelPort,
                                                                          final CancelClaimBacenPort cancelClaimBacenPort,
                                                                          final CancelClaimPort cancelClaimPort,
                                                                          final ExecutionPort executionPort,
                                                                          final SendToCancelPortabilityPort sendToCancelPortabilityPort) {
        return new OverduePortabilityClaimUseCase(findClaimToCancelPort, cancelClaimBacenPort, cancelClaimPort, executionPort, sendToCancelPortabilityPort);
    }

}
