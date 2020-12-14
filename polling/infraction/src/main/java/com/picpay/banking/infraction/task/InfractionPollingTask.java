/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.task;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.usecase.infraction.InfractionPollingUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@Component
public class InfractionPollingTask implements ApplicationRunner {

    private final String ispb;
    private final Integer limit;
    private final InfractionPollingUseCase infractionPollingUseCase;

    public InfractionPollingTask(@Value("${picpay.ispb}") final String ispb,
                                 @Value("${picpay.polling.infraction.limit}") final Integer limit,
                                 final InfractionPollingUseCase infractionPollingUseCase) {
        this.ispb = ispb;
        this.limit = limit;
        this.infractionPollingUseCase = infractionPollingUseCase;
    }

    @Trace(dispatcher = true, metricName = "InfractionReportListPolling")
    @Override
    public void run(final ApplicationArguments args) throws Exception {
        infractionPollingUseCase.execute(ispb, limit);
    }

}
