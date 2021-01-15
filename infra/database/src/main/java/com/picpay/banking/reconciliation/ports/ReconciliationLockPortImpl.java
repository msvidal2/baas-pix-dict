/*
 *  baas-pix-dict 1.0 14/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author rafael.braga
 * @version 1.0 14/01/2021
 */
@Component
@Slf4j
public class ReconciliationLockPortImpl implements ReconciliationLockPort {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String lockKey;
    private final Long lockDuration;

    @Autowired
    public ReconciliationLockPortImpl(final RedisTemplate<String, Object> redisTemplate,
                                      @Value("${pix.bacen.dict.sync.lockKey}") final String lockKey,
                                      @Value("${pix.bacen.dict.sync.lockDuration}") final Long lockDuration) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
        this.lockDuration = lockDuration;
    }

    @Override
    public void lock() {
        redisTemplate.opsForValue().set(lockKey, true);
        redisTemplate.expire(lockKey, lockDuration, TimeUnit.HOURS);
        log.info("DICT locked at {} for {} hours", LocalDateTime.now(), lockDuration);
    }

    @Override
    public void unlock() {
        redisTemplate.delete(lockKey);
        log.info("DICT unlocked at {}", LocalDateTime.now());
    }

}
