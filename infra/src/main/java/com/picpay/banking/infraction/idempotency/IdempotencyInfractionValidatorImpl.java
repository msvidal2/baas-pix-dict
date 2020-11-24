/*
 *  baas-pix-dict 1.0 11/23/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.infraction.exception.DuplicatedInfractionReportException;
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
public class IdempotencyInfractionValidatorImpl implements IdempotencyValidator<com.picpay.banking.pix.core.domain.InfractionReport> {

    private static final String HASH_NAME = "INFRACTION";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<com.picpay.banking.pix.core.domain.InfractionReport> validate(final String idempotencyKey,
                                                                                  final com.picpay.banking.pix.core.domain.InfractionReport compareTo) {
        com.picpay.banking.pix.core.domain.InfractionReport foundReport = get(idempotencyKey);

        if (foundReport == null)
            return Optional.empty();
        if (foundReport.equals(compareTo))
            return Optional.of(foundReport);

        throw new DuplicatedInfractionReportException(idempotencyKey);
    }

    public com.picpay.banking.pix.core.domain.InfractionReport get(final String idempotencyKey) {
        Object value = redisTemplate.opsForHash().get(HASH_NAME, idempotencyKey);
        return objectMapper.convertValue(value, com.picpay.banking.pix.core.domain.InfractionReport.class);
    }


}
