/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.config.TimeLimiterExecutor;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.infraction.clients.InfractionBacenClient;
import com.picpay.banking.infraction.dto.response.ListInfractionReportsResponse;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.bacen.ListInfractionPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@Component
@RequiredArgsConstructor
public class ListInfractionPortImpl implements ListInfractionPort {

    private final InfractionBacenClient infractionBacenClient;
    private final TimeLimiterExecutor timeLimiterExecutor;
    private static final String CIRCUIT_BREAKER = "list-infraction";

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public List<InfractionReport> list(final String ispb, final Integer limit) {
        ListInfractionReportsResponse response = timeLimiterExecutor.execute(CIRCUIT_BREAKER,
                                                                            () -> infractionBacenClient.listInfractions(ispb, limit),
                                                                            UUID.randomUUID().toString());
        return ListInfractionReportsResponse.toInfractionReportList(response, ispb);
    }

    public List<InfractionReport> fallback(final String ispb, final Integer limit, final Exception e) {
        throw BacenExceptionBuilder.from(e).build();
    }

}
