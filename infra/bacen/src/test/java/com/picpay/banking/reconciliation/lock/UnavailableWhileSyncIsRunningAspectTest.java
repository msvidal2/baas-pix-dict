/*
 *  baas-pix-dict 1.0 14/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.reconciliation.lock;

import com.picpay.banking.pix.core.exception.UnavailableWhileSyncIsRunningException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnavailableWhileSyncIsRunningAspectTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> opsForValue;
    private UnavailableWhileSyncIsRunningAspect aspect;

    @BeforeEach
    void setUp() {
        final String dictLockedKey = "dict::bacen::sync:active";
        aspect = new UnavailableWhileSyncIsRunningAspect(dictLockedKey, redisTemplate);
    }

    @Test
    void when_redis_locked_then_service_not_available() {
        when(redisTemplate.opsForValue()).thenReturn(opsForValue);
        when(opsForValue.get(anyString())).thenReturn("true");

        assertThatThrownBy(() -> aspect.isServiceAvailable())
            .isInstanceOf(UnavailableWhileSyncIsRunningException.class)
            .hasMessageContaining("DICT-API is currently syncing with BACEN and cannot serve requests. Please try again later");
    }

    @Test
    void when_redis_unlocked_then_service_not_available() {
        when(redisTemplate.opsForValue()).thenReturn(opsForValue);
        when(opsForValue.get(anyString())).thenReturn(null);

        assertThatCode(() -> aspect.isServiceAvailable())
            .doesNotThrowAnyException();
    }

}