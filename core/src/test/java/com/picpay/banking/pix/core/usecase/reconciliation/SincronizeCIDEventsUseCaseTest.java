package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import com.picpay.banking.pix.core.domain.reconciliation.SyncBacenCidEvents;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifier;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncBacenCidEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SincronizeCIDEventsUseCaseTest {

    @Mock
    private SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase;
    @Mock
    private SyncBacenCidEventsPort syncBacenCidEventsPort;
    @Mock
    private SyncVerifierPort syncVerifierPort;
    @Mock
    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;

    @BeforeEach
    void beforeEach() {
        sincronizeCIDEventsUseCase = new SincronizeCIDEventsUseCase(syncBacenCidEventsPort,
            syncVerifierPort, bacenContentIdentifierEventsPort);
    }

    @Test
    @DisplayName("Sincroniza os dados com a base zerada e retorno do bacen vazio")
    void synchronizes_the_data_with_the_zeroed_base_and_empty_bacen_return() {
        var expectedDate = LocalDateTime.of(2020, 1, 1, 0, 0);

        when(syncBacenCidEventsPort.getSyncBacenCid(any())).thenReturn(Optional.empty());
        when(bacenContentIdentifierEventsPort.list(any(), any())).thenReturn(new HashSet<>());

        sincronizeCIDEventsUseCase.syncByKeyType(KeyType.CPF);

        verify(syncBacenCidEventsPort, times(1)).saveAllBacenCidEvent(any(),
            argThat(argument -> argument.size() == 0));
        verify(syncBacenCidEventsPort, times(1)).saveSyncBacenCidEvents(
            argThat(argument -> argument.getLastSyncWithBacen().equals(expectedDate)));
    }

    @Test
    @DisplayName("Sincroniza os dados com a base zerada com retorno do bacen")
    void synchronizes_the_data_with_the_zeroed_base_and_with_bacen_return() {
        var dateMostRecent = LocalDateTime.now().plus(Duration.ofDays(1));

        when(syncBacenCidEventsPort.getSyncBacenCid(any())).thenReturn(Optional.empty());
        when(bacenContentIdentifierEventsPort.list(any(), any())).thenReturn(
            Set.of(BacenCidEvent.builder()
                    .cid("1")
                    .action(ReconciliationAction.ADDED)
                    .eventOnBacenAt(LocalDateTime.now())
                    .build(),
                BacenCidEvent.builder()
                    .cid("1")
                    .action(ReconciliationAction.REMOVED)
                    .eventOnBacenAt(LocalDateTime.now())
                    .build(),
                BacenCidEvent.builder()
                    .cid("2")
                    .action(ReconciliationAction.REMOVED)
                    .eventOnBacenAt(dateMostRecent)
                    .build()));

        sincronizeCIDEventsUseCase.syncByKeyType(KeyType.CPF);

        verify(syncBacenCidEventsPort, times(1)).saveAllBacenCidEvent(
            argThat(argument -> argument == KeyType.CPF),
            argThat(argument -> argument.size() == 3));
        verify(syncBacenCidEventsPort, times(1)).saveSyncBacenCidEvents(
            argThat(argument -> argument.getLastSyncWithBacen().equals(dateMostRecent)));
    }

    @Test
    @DisplayName("Sincroniza os dados com a base populada e com retorno do bacen")
    void synchronizes_the_data_with_the_populated_base_and_with_the_return_of_the_bacen() {
        var dateMostRecent = LocalDateTime.now().plus(Duration.ofDays(1));

        when(syncBacenCidEventsPort.getSyncBacenCid(any())).thenReturn(Optional.of(
            SyncBacenCidEvents.builder()
                .lastSyncWithBacen(LocalDateTime.now())
                .keyType(KeyType.CPF)
                .build()));
        when(bacenContentIdentifierEventsPort.list(any(), any())).thenReturn(
            Set.of(BacenCidEvent.builder()
                    .cid("1")
                    .action(ReconciliationAction.ADDED)
                    .eventOnBacenAt(LocalDateTime.now())
                    .build(),
                BacenCidEvent.builder()
                    .cid("1")
                    .action(ReconciliationAction.REMOVED)
                    .eventOnBacenAt(LocalDateTime.now())
                    .build(),
                BacenCidEvent.builder()
                    .cid("2")
                    .action(ReconciliationAction.REMOVED)
                    .eventOnBacenAt(dateMostRecent)
                    .build()));

        sincronizeCIDEventsUseCase.syncByKeyType(KeyType.CPF);

        verify(syncBacenCidEventsPort, times(1)).saveAllBacenCidEvent(
            argThat(argument -> argument == KeyType.CPF),
            argThat(argument -> argument.size() == 3));
        verify(syncBacenCidEventsPort, times(1)).saveSyncBacenCidEvents(
            argThat(argument -> argument.getLastSyncWithBacen().equals(dateMostRecent)));
    }

}
