/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.infraction.client.CreateInfractionBacenClient;
import com.picpay.banking.infraction.dto.request.CreateInfractionReportRequest;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@RequiredArgsConstructor
public class InfractionReportPortImpl implements InfractionReportPort {

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
        return response.toInfractionReport();
    }

    @Override
    public List<InfractionReport> listPending(final Integer ispb, final Integer limit) {
        return null;
    }

    @Override
    public InfractionReport find(final String infractionReportId, final Integer ispb) {
        return null;
    }

    @Override
    public InfractionReport cancel(final String infractionReportId, final Integer ispb, final String requestIdentifier) {
        return null;
    }

    @Override
    public InfractionReport analyze(final String infractionReportId, final Integer ispb, final InfractionAnalyze analyze,
                                    final String requestIdentifier) {
        return null;
    }

    @Override
    public List<InfractionReport> list(final Integer isbp, final Boolean isDebited, final Boolean isCredited,
                                       final InfractionReportSituation situation,
                                       final LocalDateTime dateStart, final LocalDateTime dateEnd, final Integer limit) {
        return null;
    }

    public InfractionReport createFallback(final InfractionReport infractionReport, final String requestIdentifier, Exception e) {
//        throw new BacenClientExceptionFactory.from(e);
//        throw JDClientExceptionFactory.from(e);
        return null;
    }

}
