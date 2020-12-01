/*
 *  baas-pix-dict 1.0 11/25/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.ports.bacen;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.infraction.client.InfractionBacenClient;
import com.picpay.banking.infraction.dto.request.CloseInfractionReportRequest;
import com.picpay.banking.infraction.dto.request.CreateInfractionReportRequest;
import com.picpay.banking.infraction.dto.response.CloseInfractionReportResponse;
import com.picpay.banking.infraction.dto.response.CreateInfractionReportResponse;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.AnalyzeInfractionReportPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 25/11/2020
 */
@Component
@RequiredArgsConstructor
public class AnalyzeInfractionReportPortImpl implements AnalyzeInfractionReportPort {

    private static final String CIRCUIT_BREAKER_CREATE_NAME = "analyze-infraction";
    private final InfractionBacenClient bacenClient;
    private final TimeLimiterExecutor timeLimiterExecutor;


    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_CREATE_NAME, fallbackMethod = "analyzeFallback")
    public InfractionReport analyze(final String infractionReportId, final Integer ispb,
        final InfractionAnalyze analyze, final String requestIdentifier) {

        final var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_CREATE_NAME,
            () -> bacenClient.close(CloseInfractionReportRequest.from(infractionReportId,ispb, analyze),infractionReportId),
            requestIdentifier);

        return response.toInfractionReport();
    }

    public InfractionReport analyzeFallback(final String infractionReportId, final Integer ispb,
        final InfractionAnalyze analyze, final String requestIdentifier, Exception e) {
        throw BacenExceptionBuilder.from(e).build();
    }

}
