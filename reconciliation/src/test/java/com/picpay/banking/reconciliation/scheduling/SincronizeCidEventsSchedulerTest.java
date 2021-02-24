/*
 *  baas-pix-dict 1.0 24/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.reconciliation.scheduling;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.usecase.reconciliation.SincronizeCIDEventsUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SincronizeCidEventsSchedulerTest {

    @Mock
    private SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase;
    @InjectMocks
    private SincronizeCidEventsScheduler scheduler;

    @Test
    void when_scheduled_without_key_type_then_execute_for_all_key_types() {
        doNothing().when(sincronizeCIDEventsUseCase).syncByKeyType(any(KeyType.class));
        scheduler.run();

        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.CPF);
        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.CNPJ);
        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.EMAIL);
        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.CELLPHONE);
        verify(sincronizeCIDEventsUseCase).syncByKeyType(KeyType.RANDOM);

    }

}