package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.SyncVerifier;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.SyncVerifierResult;
import com.picpay.banking.pix.core.domain.SyncVerifierResultType;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.util.ContentIdentifierUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FailureReconciliationSyncUseCaseTest {

    @Mock
    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    @Mock
    private FindPixKeyPort findPixKeyPort;
    @Mock
    private SyncVerifierPort syncVerifierPort;
    @Mock
    private BacenSyncVerificationsPort bacenSyncVerificationsPort;
    @Mock
    private SyncVerifierHistoricPort syncVerifierHistoricPort;
    @Mock
    private SyncVerifierHistoricActionPort syncVerifierHistoricActionPort;
    @Mock
    private SavePixKeyPort savePixKeyPort;
    @Mock
    private RemovePixKeyPort removePixKeyPort;
    @Mock
    private PixKeyEventPort pixKeyEventPort;

    private FailureReconciliationSyncUseCase failureReconciliationSyncUseCase;

    @BeforeEach
    private void beforeEach() {
        failureReconciliationSyncUseCase = new FailureReconciliationSyncUseCase(bacenContentIdentifierEventsPort,
            findPixKeyPort, syncVerifierPort, bacenSyncVerificationsPort, syncVerifierHistoricPort,
            syncVerifierHistoricActionPort, savePixKeyPort, removePixKeyPort, pixKeyEventPort);
    }

    @Test
    @DisplayName("Quando o vsyncHistoric é nulo lança exception")
    void exception_when_vsync_historic_is_null() {
        assertThatThrownBy(() -> failureReconciliationSyncUseCase.execute(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("VsyncHistoric cannot be null");
    }

    @Test
    @DisplayName("Quando o keyType é nulo lança exception")
    void exception_when_key_type_is_null() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();
        assertThatThrownBy(() -> failureReconciliationSyncUseCase.execute(syncVerifierHistoric))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The attribute KeyType of VsyncHistoric cannot be null");
    }

    @Test
    @DisplayName("Quando o SynchronizedAt é nulo lança exception")
    void exception_when_synchronized_at_is_null() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .keyType(KeyType.CPF)
            .build();

        assertThatThrownBy(() -> failureReconciliationSyncUseCase.execute(syncVerifierHistoric))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The attribute SynchronizedAt of VsyncHistoric cannot be null");
    }

    @Test
    @DisplayName("Criar Key quando ela existe no Bacen e não existe no database")
    void create_when_exists_in_bacen_and_not_exists_in_database() {
        when(bacenContentIdentifierEventsPort.list(any(), any()))
            .thenReturn(Set.of(ContentIdentifierUtil.bacenCidEventAdd("01")));
        when(findPixKeyPort.findByCid(any()))
            .thenReturn(Optional.empty());
        when(findPixKeyPort.findPixKey(any()))
            .thenReturn(Optional.empty());
        when(bacenContentIdentifierEventsPort.getPixKey(any()))
            .thenReturn(Optional.of(PixKey.builder()
                .key("1")
                .type(KeyType.CPF)
                .cid("1")
                .build()));
        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.of(SyncVerifier.builder().keyType(KeyType.CPF).build()));
        when(bacenSyncVerificationsPort.syncVerification(any(), any()))
            .thenReturn(SyncVerifierResult.builder().syncVerifierResultType(SyncVerifierResultType.OK).build());

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .synchronizedStart(LocalDateTime.now())
            .keyType(KeyType.CPF)
            .build();
        failureReconciliationSyncUseCase.execute(syncVerifierHistoric);

        verify(syncVerifierHistoricActionPort, times(1)).save(any());
        verify(savePixKeyPort, times(1)).savePixKey(any(), any());
        verify(removePixKeyPort, times(0)).removeByCid(any());
    }

    @Test
    @DisplayName("Atualizar Key quando ela existe no Bacen e existe no database com outros valores")
    void update_when_exists_in_bacen_and_exists_diff_in_database() {
        when(bacenContentIdentifierEventsPort.list(any(), any()))
            .thenReturn(Set.of(ContentIdentifierUtil.bacenCidEventAdd("01")));
        when(findPixKeyPort.findByCid(any()))
            .thenReturn(Optional.empty());
        when(findPixKeyPort.findPixKey(any()))
            .thenReturn(Optional.of(PixKey.builder().cid("02").build()));
        when(bacenContentIdentifierEventsPort.getPixKey(any()))
            .thenReturn(Optional.of(PixKey.builder()
                .key("1")
                .type(KeyType.CPF)
                .cid("01")
                .build()));
        when(findPixKeyPort.findPixKey(any()))
            .thenReturn(Optional.of(PixKey.builder().cid("02").build()));
        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.of(SyncVerifier.builder().keyType(KeyType.CPF).build()));
        when(bacenSyncVerificationsPort.syncVerification(any(), any()))
            .thenReturn(SyncVerifierResult.builder().syncVerifierResultType(SyncVerifierResultType.OK).build());

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .synchronizedStart(LocalDateTime.now())
            .keyType(KeyType.CPF)
            .build();
        failureReconciliationSyncUseCase.execute(syncVerifierHistoric);

        verify(syncVerifierHistoricActionPort, times(2)).save(any());
        verify(savePixKeyPort, times(1)).savePixKey(any(), any());
        verify(removePixKeyPort, times(0)).removeByCid(any());
    }

    @Test
    @DisplayName("Remover a Key quando foi removida do Bacen e não foi removida do database")
    void remove_when_exists_in_database_and_not_exists_in_bacen() {
        when(bacenContentIdentifierEventsPort.list(any(), any()))
            .thenReturn(Set.of(ContentIdentifierUtil.bacenCidEventRemove("01")));
        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(Optional.of(SyncVerifier.builder().keyType(KeyType.CPF).build()));
        when(bacenSyncVerificationsPort.syncVerification(any(), any()))
            .thenReturn(SyncVerifierResult.builder().syncVerifierResultType(SyncVerifierResultType.OK).build());

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .synchronizedStart(LocalDateTime.now())
            .keyType(KeyType.CPF)
            .build();
        failureReconciliationSyncUseCase.execute(syncVerifierHistoric);

        verify(syncVerifierHistoricActionPort, times(1)).save(any());
        verify(savePixKeyPort, times(0)).savePixKey(any(), any());
        verify(removePixKeyPort, times(1)).removeByCid(any());
    }

}
