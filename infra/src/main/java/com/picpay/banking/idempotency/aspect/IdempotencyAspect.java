/*
 *  baas-pix-dict 1.0 11/27/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.idempotency.aspect;

import com.picpay.banking.idempotency.annotation.IdempotencyKey;
import com.picpay.banking.idempotency.annotation.ValidateIdempotency;
import com.picpay.banking.pix.core.exception.IdempotencyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * This aspect validates idempotency for a given type.
 * When a method is annotated with @ValidateIdempotency, it tries to locate a target object in a Redis Cache, using the value of the argument annotated
 * with @IdempotencyKey as the hash key on Redis.
 * If a value is found, it compares the found value with the current method parameter. If a cache result is found, but the value doesn't match, then an
 * IdempotencyException is thrown. If it matches, then the value is returned directly from the cache and the method execution is cut short.
 * To find the comparison target, the aspects looks for an object that matches the type provided for the annotation.
 * @author rafael.braga
 * @version 1.0 27/11/2020
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class IdempotencyAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Around(value = "execution(* *(..)) && @annotation(com.picpay.banking.idempotency.annotation.ValidateIdempotency)")
    public Object validate(final ProceedingJoinPoint point) throws Throwable {
        final ValidateIdempotency validateIdempotency = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(ValidateIdempotency.class);
        Optional<?> comparisonTarget = comparisonTarget(validateIdempotency, point.getArgs());
        Object fromCache = fromCache(idempotencyKey(point), comparisonTarget);

        if (fromCache != null) {
            if (!match(comparisonTarget, fromCache)) {
                throw new IdempotencyException();
            }
            return fromCache;
        }

        return point.proceed();
    }

    private boolean match(final Optional<?> comparisonTarget, final Object target) {
        return comparisonTarget.map(ct -> ct.getClass().cast(target))
            .map(t -> t.equals(comparisonTarget))
            .orElse(Boolean.FALSE);
    }

    private Object fromCache(final String idempotencyKey, Optional<?> hashName) {
        Object targetClass = hashName.orElse(null);
        if (targetClass != null) {
            return redisTemplate.opsForHash().get(targetClass.getClass().getSimpleName(), idempotencyKey);
        }
        return null;
    }

    private Optional<?> comparisonTarget(ValidateIdempotency validateIdempotency, Object[] args) {
        Class<?> value = validateIdempotency.value();
        return Arrays.stream(args)
            .filter(value::isInstance)
            .findFirst()
            .map(value::cast);
    }

    private String idempotencyKey(final ProceedingJoinPoint point) {
        Object[] args = point.getArgs();
        MethodSignature methodSignature = (MethodSignature) point.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (!(annotation instanceof IdempotencyKey))
                    continue;
                return (String) args[argIndex];
            }
        }

        return null;
    }

}
