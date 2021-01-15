/*
 *  baas-pix-dict 1.0 13/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.reconciliation.lock;

import com.picpay.banking.pix.core.exception.UnavailableWhileSyncIsRunningException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * This aspects checks if the DICT sync is executing. If so, then it blocks all requests to the DICT - API application until the sync has finished its
 * execution.
 *
 * @author rafael.braga
 * @version 1.0 13/01/2021
 */
@Component
@Aspect
@Slf4j
public class UnavailableWhileSyncIsRunningAspect {

    private final String dictLockedKey;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UnavailableWhileSyncIsRunningAspect(@Value("${pix.bacen.dict.sync.lockKey}") final String dictLockedKey,
                                               final RedisTemplate<String, Object> redisTemplate) {
        this.dictLockedKey = dictLockedKey;
        this.redisTemplate = redisTemplate;
    }

    @Before("execution(public * *(..)) && within(@com.picpay.banking.pix.core.validators.reconciliation.lock.UnavailableWhileSyncIsActive *)")
    public void isServiceAvailable() {
        if (isSyncActive()) {
            log.info("Rejecting DICT request due to active synchronization");
            throw new UnavailableWhileSyncIsRunningException();
        }
    }

    private boolean isSyncActive() {
        return redisTemplate.opsForValue().get(dictLockedKey) != null;
    }

}
