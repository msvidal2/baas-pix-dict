package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifier;
import com.picpay.banking.pix.core.domain.SyncVerifierResult;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.ReconciliationBacenPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.FailureReconciliationMessagePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReconciliationSyncUseCaseTest {

    @Mock
    private SyncVerifierPort syncVerifierPort;
    @Mock
    private SyncVerifierHistoricPort syncVerifierHistoricPort;
    @Mock
    private ContentIdentifierEventPort contentIdentifierEventPort;
    @Mock
    private ReconciliationBacenPort reconciliationBacenPort;
    @Mock
    private FailureReconciliationMessagePort failureReconciliationMessagePort;

    private ReconciliationSyncUseCase reconciliationSyncUseCase;

    @BeforeEach
    private void beforeEach() {
        reconciliationSyncUseCase = new ReconciliationSyncUseCase(
            syncVerifierPort,
            syncVerifierHistoricPort,
            contentIdentifierEventPort,
            reconciliationBacenPort,
            failureReconciliationMessagePort);
    }

    @Test
    @DisplayName("Quando OK não deve executar nenhuma ação")
    void when_ok_no_action_happens() {
        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.empty());

        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.empty());

        when(contentIdentifierEventPort.findAllAfterLastSuccessfulVsync(any(), any()))
            .thenReturn(new ArrayList<>());

        when(reconciliationBacenPort.syncVerification(any()))
            .thenReturn(SyncVerifierResult.OK);

        reconciliationSyncUseCase.execute();

        verify(syncVerifierPort, times(5)).update(any());
        verify(syncVerifierHistoricPort, times(5)).save(any());
        verify(failureReconciliationMessagePort, times(0)).send(any());
    }

    @Test
    @DisplayName("Quando NOK deve enviar mensagem de falha para reconciliação")
    void when_NOK_send_failure_reconciliation() {
        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.empty());

        when(syncVerifierPort.getLastSuccessfulVsync(KeyType.CPF))
            .thenReturn(Optional.ofNullable(SyncVerifier.builder().vsync("1").keyType(KeyType.CPF).synchronizedAt(LocalDateTime.now()).build()));

        when(contentIdentifierEventPort.findAllAfterLastSuccessfulVsync(any(), any()))
            .thenReturn(new ArrayList<>());

        when(reconciliationBacenPort.syncVerification(any()))
            .thenReturn(SyncVerifierResult.OK);

        when(reconciliationBacenPort.syncVerification("1"))
            .thenReturn(SyncVerifierResult.NOK);

        reconciliationSyncUseCase.execute();

        verify(syncVerifierPort, times(5)).update(any());
        verify(syncVerifierHistoricPort, times(5)).save(any());
        verify(failureReconciliationMessagePort, times(1))
            .send(argThat(argument -> argument.getKeyType().equals(KeyType.CPF)));
    }

}
