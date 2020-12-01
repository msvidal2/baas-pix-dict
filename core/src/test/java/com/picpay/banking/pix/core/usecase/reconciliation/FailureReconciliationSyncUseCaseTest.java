package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.exception.ReconciliationsException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.ReconciliationBacenPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateAccountPixKeyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.createContentIdentifier;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FailureReconciliationSyncUseCaseTest {

    @Mock
    private ReconciliationBacenPort reconciliationBacenPort;
    @Mock
    private ContentIdentifierActionPort contentIdentifierActionPort;
    @Mock
    private ContentIdentifierEventPort contentIdentifierEventPort;
    @Mock
    private CreatePixKeyUseCase createPixKeyUseCase;
    @Mock
    private UpdateAccountPixKeyUseCase updateAccountPixKeyUseCase;
    @Mock
    private RemovePixKeyUseCase removePixKeyUseCase;
    @Mock
    private FindPixKeyPort findPixKeyPort;

    private FailureReconciliationSyncUseCase failureReconciliationSyncUseCase;

    @BeforeEach
    private void beforeEach() {
        failureReconciliationSyncUseCase = new FailureReconciliationSyncUseCase(
            reconciliationBacenPort, contentIdentifierActionPort, contentIdentifierEventPort, createPixKeyUseCase, updateAccountPixKeyUseCase,
            removePixKeyUseCase, findPixKeyPort);
    }

    @Test
    @DisplayName("Quando o vsyncHistoric é nulo lança exception")
    void exception_when_vsyncHistoric_is_null() {
        assertThatThrownBy(() -> failureReconciliationSyncUseCase.execute(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("VsyncHistoric cannot be null");
    }

    @Test
    @DisplayName("Quando o keyType é nulo lança exception")
    void exception_when_keyType_is_null() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();
        assertThatThrownBy(() -> failureReconciliationSyncUseCase.execute(syncVerifierHistoric))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The attribute KeyType of VsyncHistoric cannot be null");
    }

    @Test
    @DisplayName("Quando o SynchronizedAt é nulo lança exception")
    void exception_when_synchronizedAt_is_null() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .keyType(KeyType.CPF)
            .build();

        assertThatThrownBy(() -> failureReconciliationSyncUseCase.execute(syncVerifierHistoric))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The attribute SynchronizedAt of VsyncHistoric cannot be null");
    }

    @Test
    @DisplayName("Quando nenhuma ação é encontrada, lança exception")
    void exception_when_no_actions_found() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .keyType(KeyType.CPF)
            .synchronizedStart(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> failureReconciliationSyncUseCase.execute(syncVerifierHistoric))
            .isInstanceOf(ReconciliationsException.class)
            .hasMessageStartingWith("No action found for");
    }

    @Test
    @DisplayName("Criar Key quando ela existe no Bacen e não existe no database")
    void create_when_exists_in_bacen_and_not_exists_in_database() {
        when(reconciliationBacenPort.list(any(), any(), any()))
            .thenReturn(List.of(createContentIdentifier("1")));

        when(reconciliationBacenPort.getPixKey(any()))
            .thenReturn(Optional.of(PixKey.builder().build()));

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .synchronizedStart(LocalDateTime.now())
            .keyType(KeyType.CPF)
            .build();

        failureReconciliationSyncUseCase.execute(syncVerifierHistoric);

        verify(contentIdentifierActionPort, times(1))
            .saveAll(argThat(contentIdentifierActions -> contentIdentifierActions.size() == 1));

        verify(createPixKeyUseCase, times(1))
            .execute(any(), any(), any());

        verify(updateAccountPixKeyUseCase, times(0))
            .execute(any(), any(), any());

        verify(removePixKeyUseCase, times(0))
            .execute(any(), any(), any());
    }

    @Test
    @DisplayName("Atualizar Key quando ela existe no Bacen e existe no database com outros valores")
    void update_when_exists_in_bacen_and_exists_diff_in_database() {
        when(reconciliationBacenPort.list(any(), any(), any()))
            .thenReturn(List.of(createContentIdentifier("1")));

        when(reconciliationBacenPort.getPixKey(any()))
            .thenReturn(Optional.of(PixKey.builder().build()));

        when(findPixKeyPort.findPixKey(any()))
            .thenReturn(Optional.of(PixKey.builder().build()));

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .synchronizedStart(LocalDateTime.now())
            .keyType(KeyType.CPF)
            .build();

        failureReconciliationSyncUseCase.execute(syncVerifierHistoric);

        verify(contentIdentifierActionPort, times(1))
            .saveAll(argThat(contentIdentifierActions -> contentIdentifierActions.size() == 1));

        verify(createPixKeyUseCase, times(0))
            .execute(any(), any(), any());

        verify(updateAccountPixKeyUseCase, times(1))
            .execute(any(), any(), any());

        verify(removePixKeyUseCase, times(0))
            .execute(any(), any(), any());
    }

    @Test
    @DisplayName("Remover Key quando ela existe no database e não existe no Bacen")
    void delete_when_exists_in_database_and_not_exists_in_bacen() {
        when(contentIdentifierEventPort.findAllAfterLastSuccessfulVsync(any(), any()))
            .thenReturn(List.of(createContentIdentifier("1")));

        when(contentIdentifierEventPort.findPixKeyByContentIntentifier(any()))
            .thenReturn(Optional.of(PixKey.builder().build()));

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .synchronizedStart(LocalDateTime.now())
            .keyType(KeyType.CPF)
            .build();

        failureReconciliationSyncUseCase.execute(syncVerifierHistoric);

        verify(contentIdentifierActionPort, times(1))
            .saveAll(argThat(contentIdentifierActions -> contentIdentifierActions.size() == 1));

        verify(createPixKeyUseCase, times(0))
            .execute(any(), any(), any());

        verify(updateAccountPixKeyUseCase, times(0))
            .execute(any(), any(), any());

        verify(removePixKeyUseCase, times(1))
            .execute(any(), any(), any());
    }

}
