/*
 *  baas-pix-dict 1.0 22/12/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.claim.task;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.usecase.claim.PollingOverduePossessionClaimClaimerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 22/12/2020
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PossessionClaimForDonorTask implements ApplicationRunner {

    @Value("${picpay.ispb}")
    private Integer ispb;

    @Value("${picpay.polling.claim.limit}")
    private Integer limit;

    private final PollingOverduePossessionClaimClaimerUseCase pollingOverduePossessionClaimClaimerUseCase;

    @Override
    @Trace(dispatcher = true, metricName = "overduePossessionClaimForDonorTask")
    public void run(final ApplicationArguments args) throws Exception {
        pollingOverduePossessionClaimClaimerUseCase.executeForClaimer(ispb, limit);
    }

}
