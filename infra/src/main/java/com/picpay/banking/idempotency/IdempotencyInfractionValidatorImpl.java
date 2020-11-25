/*
 *  baas-pix-dict 1.0 11/23/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.exception.InfractionReportError;
import com.picpay.banking.pix.core.exception.InfractionReportException;
import com.picpay.banking.pix.core.validators.idempotency.IdempotencyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author rafael.braga
 * @version 1.0 23/11/2020
 */
@Service
@RequiredArgsConstructor
public class IdempotencyInfractionValidatorImpl implements IdempotencyValidator<InfractionReport> {

    private static final String HASH_NAME = "INFRACTION";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<InfractionReport> validate(final String idempotencyKey,
                                               final InfractionReport compareTo) {
        InfractionReport foundReport = get(idempotencyKey);

        if (foundReport == null)
            return Optional.empty();
        if (foundReport.equals(compareTo))
            return Optional.of(foundReport);

        throw new InfractionReportException(InfractionReportError.INFRACTION_REPORT_CONFLICT);
    }

    private InfractionReport get(final String idempotencyKey) {
        Object value = redisTemplate.opsForHash().get(HASH_NAME, idempotencyKey);
        return objectMapper.convertValue(value, InfractionReport.class);
    }

}
