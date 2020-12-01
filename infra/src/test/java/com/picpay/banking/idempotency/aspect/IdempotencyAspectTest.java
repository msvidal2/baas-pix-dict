/*
 *  baas-pix-dict 1.0 12/1/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.idempotency.aspect;

import com.picpay.banking.infraction.client.CreateInfractionBacenClient;
import com.picpay.banking.infraction.dto.request.InfractionReportRequest;
import com.picpay.banking.infraction.dto.response.CreateInfractionReportResponse;
import com.picpay.banking.infraction.dto.response.Status;
import com.picpay.banking.infraction.ports.bacen.CreateInfractionReportPortImpl;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.exception.IdempotencyException;
import com.picpay.banking.pix.core.ports.infraction.CreateInfractionReportPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdempotencyAspectTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private CreateInfractionBacenClient bacenClient;
    @Mock
    private TimeLimiterExecutor timeLimiterExecutor;
    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    private AspectJProxyFactory factory;

    @BeforeEach
    void setUp() {
        CreateInfractionReportPortImpl createInfractionReportPort = new CreateInfractionReportPortImpl(bacenClient, timeLimiterExecutor);
        factory = new AspectJProxyFactory(createInfractionReportPort);
        factory.addAspect(new IdempotencyAspect(redisTemplate));
    }

    @Test
    void when_value_is_present_but_is_not_a_match_then_should_throw_idempotency_exception() {
        CreateInfractionReportPort proxy = factory.getProxy();

        InfractionReport infractionReport = getInfractionReport("E9999901012341234123412345678999");
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(), any())).thenReturn(infractionReport);

        InfractionReport differentReport = getInfractionReport("E9999901012341234123412345678900");

        assertThatThrownBy(() -> proxy.create(differentReport, "id"))
            .isInstanceOf(IdempotencyException.class);

        verify(timeLimiterExecutor, times(0)).execute(any(), any(), any());
    }

    @Test
    void when_value_is_not_present_on_cache_should_call_actual_method() {
        CreateInfractionReportPort proxy = factory.getProxy();

        InfractionReport infractionReport = getInfractionReport("E9999901012341234123412345678900");
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(), any())).thenReturn(null);
        when(timeLimiterExecutor.execute(anyString(), any(), any())).thenReturn(response(infractionReport));

        proxy.create(infractionReport, "id");

        verify(timeLimiterExecutor).execute(any(), any(), any());
    }

    @Test
    void when_value_is_present_on_cache_and_value_matches_should_return_from_aspect_cache() {
        CreateInfractionReportPort proxy = factory.getProxy();

        InfractionReport infractionReport = getInfractionReport("E9999901012341234123412345678900");
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(), any())).thenReturn(infractionReport);

        proxy.create(infractionReport, "id");

        verify(timeLimiterExecutor, times(0)).execute(any(), any(), any());
    }

    private CreateInfractionReportResponse response(InfractionReport infractionReport) {
        return CreateInfractionReportResponse.builder()
            .infractionReportRequest(InfractionReportRequest.builder()
                                         .infractionType(com.picpay.banking.infraction.dto.request.InfractionType.FRAUD)
                                         .reportDetails(infractionReport.getDetails())
                                         .transactionId(infractionReport.getTransactionId())
                                         .creationTime(LocalDateTime.now())
                                         .creditedParticipant(String.valueOf(infractionReport.getIspbCredited()))
                                         .debitedParticipant(String.valueOf(infractionReport.getIspbDebited()))
                                         .lastModified(LocalDateTime.now())
                                         .id("F6DF6B32-6A64-4B1F-8A82-BA4DF77EECC7")
                                         .reportedBy(ReportedBy.CREDITED_PARTICIPANT)
                                         .status(Status.OPEN)
                                         .build())
            .build();
    }

    private InfractionReport getInfractionReport(String endToEndId) {
        return InfractionReport.builder()
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
            .endToEndId(endToEndId)
            .situation(InfractionReportSituation.OPEN)
            .infractionType(InfractionType.FRAUD)
            .reportedBy(ReportedBy.CREDITED_PARTICIPANT)
            .ispbDebited(1234)
            .ispbCredited(56789)
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .build();
    }

}