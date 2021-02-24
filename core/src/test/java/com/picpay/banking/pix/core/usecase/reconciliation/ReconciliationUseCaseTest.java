/*
 *  baas-pix-dict 1.0 23/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierResultType;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierResultType.NOK;
import static com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierResultType.OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReconciliationUseCaseTest {

    @Mock
    private FailureReconciliationSyncByFileUseCase syncByFileUseCase;
    @Mock
    private ReconciliationSyncUseCase reconciliationSyncUseCase;
    @Mock
    private FailureReconciliationSyncUseCase failureReconciliationSyncUseCase;
    @Mock
    private SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase;
    @Mock
    private ReconciliationLockPort reconciliationLockPort;

    @InjectMocks
    private ReconciliationUseCase reconciliationUseCase;

    @BeforeEach
    void setUp() {
        doNothing().when(reconciliationLockPort).lock();
        doNothing().when(reconciliationLockPort).unlock();
        doNothing().when(sincronizeCIDEventsUseCase).syncByKeyType(any());
    }

    @Test
    void when_execute_run_by_events_and_no_sync_is_needed_then_dont_run_sync() {
        SyncVerifierHistoric syncResultOK = syncResult(OK);
        when(reconciliationSyncUseCase.execute(any(KeyType.class))).thenReturn(syncResultOK);

        assertThatCode(() -> {
            SyncVerifierHistoric result = reconciliationUseCase.execute(KeyType.CPF);
            assertThat(result.getSyncVerifierResultType()).isEqualTo(OK);
        }).doesNotThrowAnyException();

        verify(reconciliationLockPort).lock();
        verify(failureReconciliationSyncUseCase, never()).execute(any(SyncVerifierHistoric.class));
        verify(syncByFileUseCase, never()).execute(any(KeyType.class));
        verify(reconciliationLockPort).unlock();
    }

    @Test
    void when_sync_is_not_ok_then_run_failure_sync() {
        when(reconciliationSyncUseCase.execute(any(KeyType.class))).thenReturn(syncResult(NOK));
        when(failureReconciliationSyncUseCase.execute(any(SyncVerifierHistoric.class))).thenReturn(syncResult(OK));

        assertThatCode(() -> {
            SyncVerifierHistoric result = reconciliationUseCase.execute(KeyType.CPF);
            assertThat(result.getSyncVerifierResultType()).isEqualTo(OK);
        }).doesNotThrowAnyException();

        verify(reconciliationLockPort).lock();
        verify(failureReconciliationSyncUseCase).execute(any(SyncVerifierHistoric.class));
        verify(syncByFileUseCase, never()).execute(any(KeyType.class));
        verify(reconciliationLockPort).unlock();
    }

    @Test
    void when_failure_sync_doesnt_solve_issues_then_run_file_sync() {
        when(reconciliationSyncUseCase.execute(any(KeyType.class))).thenReturn(syncResult(NOK));
        when(failureReconciliationSyncUseCase.execute(any(SyncVerifierHistoric.class))).thenReturn(syncResult(NOK));
        when(syncByFileUseCase.execute(any(KeyType.class))).thenReturn(syncResult(OK));

        assertThatCode(() -> {
            SyncVerifierHistoric result = reconciliationUseCase.execute(KeyType.CPF);
            assertThat(result.getSyncVerifierResultType()).isEqualTo(OK);
        }).doesNotThrowAnyException();

        verify(reconciliationLockPort).lock();
        verify(failureReconciliationSyncUseCase).execute(any(SyncVerifierHistoric.class));
        verify(syncByFileUseCase).execute(any(KeyType.class));
        verify(reconciliationLockPort).unlock();
    }

    private SyncVerifierHistoric syncResult(final SyncVerifierResultType result) {
        return SyncVerifierHistoric
            .builder()
            .syncVerifierResultType(result)
            .build();
    }

}