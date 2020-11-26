/*
 *  baas-pix-dict 1.0 11/25/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.ports.bacen;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.infraction.client.CreateInfractionBacenClient;
import com.picpay.banking.infraction.dto.request.CreateInfractionReportRequest;
import com.picpay.banking.infraction.dto.response.CreateInfractionReportResponse;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.CreateInfractionReportPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 25/11/2020
 */
@Component
@RequiredArgsConstructor
public class CreateInfractionReportPortImpl implements CreateInfractionReportPort {

    private static final String CIRCUIT_BREAKER_CREATE_NAME = "create-infraction";
    private final CreateInfractionBacenClient bacenClient;
    private final TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_CREATE_NAME, fallbackMethod = "createFallback")
    public InfractionReport create(final InfractionReport infractionReport, final String requestIdentifier) {
        CreateInfractionReportRequest requestDto = CreateInfractionReportRequest.from(infractionReport);
        final var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_CREATE_NAME,
                                                         () -> bacenClient.create(requestDto),
                                                         requestIdentifier);
        return CreateInfractionReportResponse.toInfractionReport(response);
    }

    public InfractionReport createFallback(final InfractionReport infractionReport, final String requestIdentifier, Exception e) {
        throw BacenExceptionBuilder.from(e).build();
    }

}
