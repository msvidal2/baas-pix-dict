package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Vsync;
import com.picpay.banking.pix.core.ports.reconciliation.BacenReconciliationPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseReconciliationPort;
import com.picpay.banking.pix.core.ports.reconciliation.FailureMessagePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReconciliationSyncUseCaseTest {

    @Mock
    private DatabaseReconciliationPort databaseReconciliationPort;
    @Mock
    private BacenReconciliationPort bacenReconciliationPort;
    @Mock
    private FailureMessagePort failureMessagePort;

    @Test
    @DisplayName("Quando OK não deve executar nenhuma ação")
    public void when_ok_no_action_happens() {
        when(databaseReconciliationPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.ofNullable(null));

        when(databaseReconciliationPort.listAfterLastSuccessfulVsync(any(), any()))
            .thenReturn(new ArrayList<>());

        when(bacenReconciliationPort.syncVerification(any()))
            .thenReturn(Vsync.VsyncResult.OK);

        ReconciliationSyncUseCase reconciliationSyncUseCase = new ReconciliationSyncUseCase(
            databaseReconciliationPort,
            bacenReconciliationPort,
            failureMessagePort);

        reconciliationSyncUseCase.execute();

        verify(failureMessagePort, times(0)).sendMessageForSincronization(any());
    }

    @Test
    @DisplayName("Quando OK não deve executar nenhuma ação")
    public void run() {
        when(databaseReconciliationPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.ofNullable(null));

        when(databaseReconciliationPort.getLastSuccessfulVsync(KeyType.CPF))
            .thenReturn(Optional.ofNullable(Vsync.builder().vsync("1").keyType(KeyType.CPF).synchronizedAt(LocalDateTime.now()).build()));

        when(databaseReconciliationPort.listAfterLastSuccessfulVsync(any(), any()))
            .thenReturn(new ArrayList<>());

        when(bacenReconciliationPort.syncVerification(any()))
            .thenReturn(Vsync.VsyncResult.OK);

        ReconciliationSyncUseCase reconciliationSyncUseCase = new ReconciliationSyncUseCase(
            databaseReconciliationPort,
            bacenReconciliationPort,
            failureMessagePort);

        reconciliationSyncUseCase.execute();

        verify(failureMessagePort, times(0)).sendMessageForSincronization(any());

        //verify(reconciliationSyncUseCase).execute(any(), argThat(argument -> pixAdd.getKey().equals(argument.getKey())), any());

        //ArgumentCaptor<PixKey> vsyncArgumentCaptor = ArgumentCaptor.forClass(PixKey.class);
        //verify(failureReconciliationSyncUseCase).execute(vsyncArgumentCaptor.capture());
        //assertThat(vsyncArgumentCaptor.getValue().getKey()).isEqualTo(pixAdd.getKey());
    }

}
