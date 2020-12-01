/*
 *  baas-pix-dict 1.0 11/25/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.core.exception.IdempotencyException;
import com.picpay.banking.pix.core.validators.idempotency.IdempotencyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

/**
 * @author rafael.braga
 * @version 1.0 25/11/2020
 */
@RequiredArgsConstructor
public class IdempotencyValidatorImpl<T> implements IdempotencyValidator<T> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Class<T> typeParameterClass;

    @Override
    public Optional<T> validate(final String idempotencyKey, final T compareTo) {
        T found = get(idempotencyKey);

        if (found == null || found.equals(compareTo))
            return Optional.ofNullable(found);

        throw new IdempotencyException();
    }

    private T get(final String idempotencyKey) {
        Object value = redisTemplate.opsForHash().get(typeParameterClass.getName(), idempotencyKey);
        return objectMapper.convertValue(value, typeParameterClass);
    }

}
