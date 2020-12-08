/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.dict.schedulers.infraction;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.usecase.infraction.InfractionPollingUseCase;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@Component
public class InfractionPollingScheduler {

    private final Integer ispb;
    private final Integer limit;
    private final InfractionPollingUseCase infractionPollingUseCase;

    public InfractionPollingScheduler(@Value("${picpay.ispb}") final Integer ispb,
                                      @Value("${picpay.polling.infraction.limit}") final Integer limit,
                                      final InfractionPollingUseCase infractionPollingUseCase) {
        this.ispb = ispb;
        this.limit = limit;
        this.infractionPollingUseCase = infractionPollingUseCase;
    }

    @Scheduled(initialDelayString = "${picpay.polling.infraction.initial-delay}",
               fixedDelayString = "${picpay.polling.infraction.fixed-delay}")
    @SchedulerLock(name = "infraction-list-polling-lock",
        lockAtLeastFor = "${picpay.polling.infraction.lock.duration}",
        lockAtMostFor = "${picpay.polling.infraction.lock.duration}")
    @Trace(dispatcher = true, metricName = "InfractionListPolling")
    public void run() {
        infractionPollingUseCase.execute(ispb, limit);
    }

}
