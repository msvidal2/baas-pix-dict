/*
 *  baas-pix-dict 1.0 22/12/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.claim.config;

import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendOverduePossessionClaimPort;
import com.picpay.banking.pix.core.usecase.claim.PollingOverduePossessionClaimUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 22/12/2020
 */
@Configuration
public class OverduePossessionClaimConfig {

    @Bean
    public PollingOverduePossessionClaimUseCase pollingOverduePossessionClaimUseCase(final FindClaimToCancelPort findClaimToCancelPort,
                                                                                     final SendOverduePossessionClaimPort sendOverduePossessionClaimPort) {
        return new PollingOverduePossessionClaimUseCase(findClaimToCancelPort, sendOverduePossessionClaimPort);
    }


}
