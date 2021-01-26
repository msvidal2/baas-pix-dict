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
import com.picpay.banking.pix.core.domain.infraction.ListInfractionReports;
import com.picpay.banking.pix.core.ports.infraction.bacen.ListInfractionPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ListInfractionPortImpl implements ListInfractionPort {

    private final InfractionBacenClient infractionBacenClient;
    private final TimeLimiterExecutor timeLimiterExecutor;
    private static final String CIRCUIT_BREAKER = "list-infraction";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public ListInfractionReports list(String ispb, Integer limit, LocalDateTime startDate, LocalDateTime endDate) {
        ListInfractionReportsResponse response = timeLimiterExecutor.execute(CIRCUIT_BREAKER,
                                                                             () -> infractionBacenClient.listInfractions(ispb,
                                                                                                                         limit,
                                                                                                                         DATE_FORMATTER.format(startDate),
                                                                                                                         DATE_FORMATTER.format(endDate),
                                                                                                                         true),
                                                                             UUID.randomUUID().toString());
        return ListInfractionReportsResponse.toInfractionReportsList(response);
    }

    public ListInfractionReports fallback(String ispb, Integer limit, LocalDateTime startDate, LocalDateTime endDate, Exception e) {
        log.error("Infraction_fallback_listingBacen",
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e).build();
    }

}
