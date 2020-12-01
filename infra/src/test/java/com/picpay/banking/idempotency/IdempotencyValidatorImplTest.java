/*
 *  baas-pix-dict 1.0 11/26/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.exception.IdempotencyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdempotencyValidatorImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Test
    void when_validate_and_found_return() {
        IdempotencyValidatorImpl<InfractionReport> idempotencyValidator = new IdempotencyValidatorImpl<>(redisTemplate, objectMapper, InfractionReport.class);
        InfractionReport infractionReport = getInfractionReport(InfractionReportSituation.OPEN);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(), any())).thenReturn(infractionReport);
        when(objectMapper.convertValue(infractionReport, InfractionReport.class)).thenReturn(infractionReport);
        Optional<InfractionReport> infraction = idempotencyValidator.validate("ikey", infractionReport);
        assertThat(infraction).isPresent();
    }

    @Test
    void when_validate_not_present_return_empty() {
        IdempotencyValidatorImpl<InfractionReport> idempotencyValidator = new IdempotencyValidatorImpl<>(redisTemplate, objectMapper, InfractionReport.class);
        InfractionReport infractionReport = getInfractionReport(InfractionReportSituation.OPEN);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(), any())).thenReturn(null);
        when(objectMapper.convertValue(null, InfractionReport.class)).thenReturn(null);
        Optional<InfractionReport> infraction = idempotencyValidator.validate("ikey", infractionReport);
        assertThat(infraction).isNotPresent();
    }

    @Test
    void when_validate_and_present_but_different_throw_exception() {
        IdempotencyValidatorImpl<InfractionReport> idempotencyValidator = new IdempotencyValidatorImpl<>(redisTemplate, objectMapper, InfractionReport.class);
        InfractionReport infractionReport = getInfractionReport(InfractionReportSituation.OPEN);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(), any())).thenReturn(infractionReport);
        when(objectMapper.convertValue(infractionReport, InfractionReport.class)).thenReturn(getInfractionReport(InfractionReportSituation.RECEIVED));
        assertThatThrownBy(() -> idempotencyValidator.validate("ikey", infractionReport))
            .isInstanceOf(IdempotencyException.class)
            .hasMessageContaining("Uma entidade com esse request identifier com body diferente já foi criada anteriormente");
    }

    private InfractionReport getInfractionReport(InfractionReportSituation situation) {
        return InfractionReport.builder()
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
            .endToEndId("E9999901012341234123412345678900")
            .situation(situation)
            .ispbDebited(1234)
            .ispbCredited(56789)
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .build();
    }

}