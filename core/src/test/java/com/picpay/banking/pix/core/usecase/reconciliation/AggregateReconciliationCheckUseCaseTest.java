package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifier;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierResult;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierResultType;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationActionPort;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AggregateReconciliationCheckUseCaseTest {

    @Mock
    private ContentIdentifierPort contentIdentifierPort;
    @Mock
    private BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    @Mock
    private FindPixKeyPort findPixKeyPort;
    @Mock
    private SyncVerifierPort syncVerifierPort;
    @Mock
    private BacenSyncVerificationsPort bacenSyncVerificationsPort;
    @Mock
    private SyncVerifierHistoricPort syncVerifierHistoricPort;
    @Mock
    private ReconciliationActionPort reconciliationActionPort;

    private AggregateReconciliationCheckUseCase aggregateReconciliationCheckUseCase;

    @BeforeEach
    private void beforeEach() {
        aggregateReconciliationCheckUseCase = new AggregateReconciliationCheckUseCase(
            contentIdentifierPort,
            bacenPixKeyByContentIdentifierPort,
            findPixKeyPort,
            syncVerifierPort,
            bacenSyncVerificationsPort,
            syncVerifierHistoricPort,
            reconciliationActionPort
        );
    }

    @Test
    @DisplayName("Quando o vsyncHistoric é nulo lança exception")
    void exception_when_vsync_historic_is_null() {
        assertThatThrownBy(() -> aggregateReconciliationCheckUseCase.execute(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("VsyncHistoric cannot be null");
    }

    @Test
    @DisplayName("Quando o keyType é nulo lança exception")
    void exception_when_key_type_is_null() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();
        assertThatThrownBy(() -> aggregateReconciliationCheckUseCase.execute(syncVerifierHistoric))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The attribute KeyType of VsyncHistoric cannot be null");
    }

    @Test
    @DisplayName("Quando o SynchronizedAt é nulo lança exception")
    void exception_when_synchronized_at_is_null() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .keyType(KeyType.CPF)
            .build();

        assertThatThrownBy(() -> aggregateReconciliationCheckUseCase.execute(syncVerifierHistoric))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The attribute SynchronizedAt of VsyncHistoric cannot be null");
    }

    @Test
    @DisplayName("Quando o syncVerifierHistoric é um caso de OK lança exception")
    void exception_when_sync_verifier_historic_has_result_OK() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .keyType(KeyType.CPF)
            .syncVerifierResultType(SyncVerifierResultType.OK)
            .synchronizedStart(LocalDateTime.now())
            .build();
        assertThatThrownBy(() -> aggregateReconciliationCheckUseCase.execute(syncVerifierHistoric))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The attribute SyncVerifierResultType of VsyncHistoric must be NOK");
    }

    @Test
    @DisplayName("Criar Key quando ela existe no Bacen e não existe no database")
    void create_when_exists_in_bacen_and_not_exists_in_database() {
        final LocalDateTime expectedResponseTime = LocalDateTime.now();
        final String expectedVsync = "01";

        when(contentIdentifierPort.findBacenEventsAfter(any(), any()))
            .thenReturn(Set.of(ContentIdentifierUtil.bacenCidEventAdd(expectedVsync)));

        when(bacenPixKeyByContentIdentifierPort.getPixKeyByCid(expectedVsync))
            .thenReturn(Optional.of(PixKey.builder()
                .key("1")
                .type(KeyType.CPF)
                .cid(expectedVsync)
                .build()));
        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(SyncVerifier.builder().keyType(KeyType.CPF).build());
        when(bacenSyncVerificationsPort.syncVerification(any(), any()))
            .thenReturn(SyncVerifierResult.builder()
                .responseTime(expectedResponseTime)
                .syncVerifierResultType(SyncVerifierResultType.OK)
                .build());

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .synchronizedStart(LocalDateTime.now())
            .keyType(KeyType.CPF)
            .syncVerifierResultType(SyncVerifierResultType.NOK)
            .build();
        aggregateReconciliationCheckUseCase.execute(syncVerifierHistoric);

        verify(reconciliationActionPort).insertPixKey(
            argThat(pixKeyToInsert -> pixKeyToInsert.getCid().equals(expectedVsync)));
        verify(reconciliationActionPort, never()).updatePixKey(any(), any());
        verify(reconciliationActionPort, never()).removePixKey(any());
        verify(bacenSyncVerificationsPort).syncVerification(KeyType.CPF, expectedVsync);
        verify(syncVerifierPort).save(argThat(syncVerifierToSave -> {
            assertThat(syncVerifierToSave.getVsync()).isEqualTo(expectedVsync);
            assertThat(syncVerifierToSave.getSynchronizedAt()).isEqualTo(expectedResponseTime);
            assertThat(syncVerifierToSave.getKeyType()).isEqualTo(KeyType.CPF);
            return true;
        }));
        verify(syncVerifierHistoricPort).save(
            argThat(argument -> argument.getReconciliationMethod() == SyncVerifierHistoric.ReconciliationMethod.FIX_BY_AGGREGATE));
        verify(syncVerifierHistoricPort).save(
            argThat(argument -> argument.getReconciliationMethod() == SyncVerifierHistoric.ReconciliationMethod.ONLY_VERIFIER));
    }

    @Test
    @DisplayName("Atualizar Key quando ela existe no Bacen e existe no database com outros valores")
    void update_when_exists_in_bacen_and_exists_diff_in_database() {
        final LocalDateTime expectedResponseTime = LocalDateTime.now();
        final String expectedVsync = "02";

        when(contentIdentifierPort.findBacenEventsAfter(any(), any()))
            .thenReturn(Set.of(ContentIdentifierUtil.bacenCidEventAdd(expectedVsync)));
        when(bacenPixKeyByContentIdentifierPort.getPixKeyByCid(expectedVsync))
            .thenReturn(Optional.of(PixKey.builder()
                .key("1")
                .type(KeyType.CPF)
                .cid(expectedVsync)
                .build()));
        when(findPixKeyPort.findPixKey("1"))
            .thenReturn(Optional.of(PixKey.builder()
                .key("1")
                .type(KeyType.CPF)
                .cid("01")
                .build()));

        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(SyncVerifier.builder().keyType(KeyType.CPF).build());
        when(bacenSyncVerificationsPort.syncVerification(any(), any()))
            .thenReturn(SyncVerifierResult.builder()
                .responseTime(expectedResponseTime)
                .syncVerifierResultType(SyncVerifierResultType.OK)
                .build());

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .synchronizedStart(LocalDateTime.now())
            .keyType(KeyType.CPF)
            .syncVerifierResultType(SyncVerifierResultType.NOK)
            .build();
        aggregateReconciliationCheckUseCase.execute(syncVerifierHistoric);

        verify(reconciliationActionPort).updatePixKey(argThat(argument -> true),
            argThat(pixKeyToUpdate -> pixKeyToUpdate.getCid().equals(expectedVsync)));
        verify(reconciliationActionPort, never()).insertPixKey(any());
        verify(reconciliationActionPort, never()).removePixKey(any());
        verify(bacenSyncVerificationsPort).syncVerification(KeyType.CPF, expectedVsync);
        verify(syncVerifierPort).save(argThat(syncVerifierToSave -> {
            assertThat(syncVerifierToSave.getVsync()).isEqualTo(expectedVsync);
            assertThat(syncVerifierToSave.getSynchronizedAt()).isEqualTo(expectedResponseTime);
            assertThat(syncVerifierToSave.getKeyType()).isEqualTo(KeyType.CPF);
            return true;
        }));
        verify(syncVerifierHistoricPort).save(
            argThat(argument -> argument.getReconciliationMethod() == SyncVerifierHistoric.ReconciliationMethod.FIX_BY_AGGREGATE));
        verify(syncVerifierHistoricPort).save(
            argThat(argument -> argument.getReconciliationMethod() == SyncVerifierHistoric.ReconciliationMethod.ONLY_VERIFIER));
    }

    @Test
    @DisplayName("Remover a Key quando foi removida do Bacen e não foi removida do database")
    void remove_when_exists_in_database_and_not_exists_in_bacen() {
        final LocalDateTime expectedResponseTime = LocalDateTime.now();
        final String expectedVsync = "03";

        when(contentIdentifierPort.findBacenEventsAfter(any(), any()))
            .thenReturn(Set.of(ContentIdentifierUtil.bacenCidEventRemove("03")));
        when(findPixKeyPort.findByCid("03")).thenReturn(Optional.of(PixKey.builder()
            .key("1")
            .type(KeyType.CPF)
            .cid("03")
            .build()));
        when(syncVerifierPort.getLastSuccessfulVsync(any()))
            .thenReturn(SyncVerifier.builder().keyType(KeyType.CPF).build());
        when(bacenSyncVerificationsPort.syncVerification(any(), any()))
            .thenReturn(SyncVerifierResult.builder()
                .responseTime(expectedResponseTime)
                .syncVerifierResultType(SyncVerifierResultType.OK)
                .build());

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .synchronizedStart(LocalDateTime.now())
            .keyType(KeyType.CPF)
            .syncVerifierResultType(SyncVerifierResultType.NOK)
            .build();
        aggregateReconciliationCheckUseCase.execute(syncVerifierHistoric);

        verify(reconciliationActionPort).removePixKey(
            argThat(pixKeyToRemove -> pixKeyToRemove.getCid().equals(expectedVsync)));
        verify(reconciliationActionPort, never()).insertPixKey(any());
        verify(reconciliationActionPort, never()).updatePixKey(any(), any());
        verify(bacenSyncVerificationsPort).syncVerification(KeyType.CPF, expectedVsync);
        verify(syncVerifierPort).save(argThat(syncVerifierToSave -> {
            assertThat(syncVerifierToSave.getVsync()).isEqualTo(expectedVsync);
            assertThat(syncVerifierToSave.getSynchronizedAt()).isEqualTo(expectedResponseTime);
            assertThat(syncVerifierToSave.getKeyType()).isEqualTo(KeyType.CPF);
            return true;
        }));
        verify(syncVerifierHistoricPort).save(
            argThat(argument -> argument.getReconciliationMethod() == SyncVerifierHistoric.ReconciliationMethod.FIX_BY_AGGREGATE));
        verify(syncVerifierHistoricPort).save(
            argThat(argument -> argument.getReconciliationMethod() == SyncVerifierHistoric.ReconciliationMethod.ONLY_VERIFIER));
    }

}
