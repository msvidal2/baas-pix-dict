/*
 *  baas-pix-dict 1.0 24/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.reconciliation.scheduling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@ExtendWith(MockitoExtension.class)
class SyncExecutionScheduleTest {

    @InjectMocks
    private SyncExecutionSchedule syncExecutionSchedule;

    @Test
    void when_requested_then_run_without_issues() {
        assertThatCode(() -> syncExecutionSchedule.run()).doesNotThrowAnyException();
    }

}