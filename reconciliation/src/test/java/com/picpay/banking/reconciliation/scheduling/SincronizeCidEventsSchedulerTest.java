/*
 *  baas-pix-dict 1.0 24/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.reconciliation.scheduling;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.exception.UnavailableWhileSyncIsRunningException;
import com.picpay.banking.pix.core.usecase.reconciliation.SincronizeCIDEventsUseCase;
import com.picpay.banking.reconciliation.lock.UnavailableWhileSyncIsRunningAspect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SincronizeCidEventsSchedulerTest {

    @Mock
    private SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    private AspectJProxyFactory factory;

    @BeforeEach
    void setUp() {
        SincronizeCidEventsScheduler scheduler = new SincronizeCidEventsScheduler(sincronizeCIDEventsUseCase);
        factory = new AspectJProxyFactory(scheduler);
        factory.addAspect(new UnavailableWhileSyncIsRunningAspect("lock", redisTemplate));
    }

    @Test
    void when_scheduled_without_key_type_then_execute_for_all_key_types() {
        SincronizeCidEventsScheduler scheduler = factory.getProxy();

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(null);

        doNothing().when(sincronizeCIDEventsUseCase).syncByKeyType(any(KeyType.class));
        scheduler.run();

        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.CPF);
        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.CNPJ);
        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.EMAIL);
        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.CELLPHONE);
        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.RANDOM);
    }

    @Test
    void when_running_at_the_same_time_that_the_syncverifier_should_do_nothing() {
        SincronizeCidEventsScheduler scheduler = factory.getProxy();

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn("lock");

        assertThatThrownBy(scheduler::run).isInstanceOf(UnavailableWhileSyncIsRunningException.class);
    }

}