package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifier;
import com.picpay.banking.pix.core.domain.SyncVerifierResult;
import com.picpay.banking.pix.core.domain.SyncVerifierResultType;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    private ContentIdentifierPort contentIdentifierPort;
    @Mock
    private BacenSyncVerificationsPort bacenSyncVerificationsPort;
    @Mock
    private ReconciliationSyncUseCase reconciliationSyncUseCase;

    @BeforeEach
    private void beforeEach() {
        reconciliationSyncUseCase = new ReconciliationSyncUseCase(
            syncVerifierPort,
            syncVerifierHistoricPort,
            contentIdentifierPort,
            bacenSyncVerificationsPort);
    }

    @Test
    @DisplayName("Quando OK não deve executar nenhuma ação")
    void when_ok_no_action_happens() {
        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.empty());

        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.empty());

        when(contentIdentifierPort.findAllCidsAfterLastSuccessfulVsync(any(), any()))
            .thenReturn(new HashSet<>());

        when(bacenSyncVerificationsPort.syncVerification(any(), any()))
            .thenReturn(SyncVerifierResult.builder()
                .syncVerifierLastModified(LocalDateTime.now())
                .syncVerifierResultType(SyncVerifierResultType.OK)
                .build());

        when(syncVerifierHistoricPort.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var syncVerifierHistoric = reconciliationSyncUseCase.execute(KeyType.CPF);

        verify(syncVerifierPort, times(1)).save(any());
        verify(syncVerifierHistoricPort, times(1)).save(any());
        assertThat(syncVerifierHistoric.isOK()).isTrue();
    }

    @Test
    @DisplayName("Quando NOK deve enviar mensagem de falha para reconciliação")
    void when_NOK_send_failure_reconciliation() {
        when(syncVerifierPort.getLastSuccessfulVsync(KeyType.CPF))
            .thenReturn(Optional.ofNullable(SyncVerifier.builder().vsync("1").keyType(KeyType.CPF).synchronizedAt(LocalDateTime.now()).build()));

        when(contentIdentifierPort.findAllCidsAfterLastSuccessfulVsync(any(), any()))
            .thenReturn(new HashSet<>());

        when(bacenSyncVerificationsPort.syncVerification(KeyType.CPF, "1"))
            .thenReturn(SyncVerifierResult.builder()
                .syncVerifierLastModified(LocalDateTime.now())
                .syncVerifierResultType(SyncVerifierResultType.NOK)
                .build());

        when(syncVerifierHistoricPort.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var syncVerifierHistoric = reconciliationSyncUseCase.execute(KeyType.CPF);

        verify(syncVerifierPort, times(1)).save(any());
        verify(syncVerifierHistoricPort, times(1)).save(any());
        assertThat(syncVerifierHistoric.isNOK()).isTrue();
    }

}
