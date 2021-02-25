package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import com.picpay.banking.pix.core.domain.reconciliation.SyncBacenCidEvents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SyncBacenCidEventsTest {

    @Test
    @DisplayName("Sincronizar com uma lista vazia")
    void sync_with_empty_list() {
        var lastSyncWithBacen = LocalDateTime.now();

        var syncBacenCidEvents = SyncBacenCidEvents.builder()
            .keyType(KeyType.CPF)
            .lastSyncWithBacen(lastSyncWithBacen)
            .build();

        syncBacenCidEvents.syncWithBacen(new HashSet<>());

        assertThat(syncBacenCidEvents.getLastSyncWithBacen()).isEqualTo(lastSyncWithBacen);
    }

    @Test
    @DisplayName("Sincronizar com um evento")
    void sync_with_one_event() {
        var lastSyncWithBacen = LocalDateTime.now();

        var syncBacenCidEvents = SyncBacenCidEvents.builder()
            .keyType(KeyType.CPF)
            .lastSyncWithBacen(lastSyncWithBacen)
            .build();

        var events = Set.of(BacenCidEvent.builder()
            .cid("1")
            .eventOnBacenAt(LocalDateTime.now())
            .action(ReconciliationAction.ADDED)
            .build());

        syncBacenCidEvents.syncWithBacen(events);

        assertThat(syncBacenCidEvents.getLastSyncWithBacen()).isEqualTo(events.iterator().next().getEventOnBacenAt());
    }

    @Test
    @DisplayName("Sincronizar com uma lista de eventos")
    void sync_with_list_of_events() {
        var lastSyncWithBacen = LocalDateTime.now();

        var syncBacenCidEvents = SyncBacenCidEvents.builder()
            .keyType(KeyType.CPF)
            .lastSyncWithBacen(lastSyncWithBacen)
            .build();

        var eventMostRecent = LocalDateTime.now().plus(Duration.ofDays(1));

        var events = new HashSet<BacenCidEvent>();
        events.add(BacenCidEvent.builder()
            .cid("1")
            .eventOnBacenAt(LocalDateTime.now())
            .action(ReconciliationAction.ADDED)
            .build());
        events.add(BacenCidEvent.builder()
            .cid("2")
            .eventOnBacenAt(LocalDateTime.now())
            .action(ReconciliationAction.REMOVED)
            .build());
        events.add(BacenCidEvent.builder()
            .cid("3")
            .eventOnBacenAt(eventMostRecent)
            .action(ReconciliationAction.ADDED)
            .build());
        events.add(BacenCidEvent.builder()
            .cid("4")
            .eventOnBacenAt(LocalDateTime.now())
            .action(ReconciliationAction.ADDED)
            .build());
        events.add(BacenCidEvent.builder()
            .cid("5")
            .eventOnBacenAt(LocalDateTime.now())
            .action(ReconciliationAction.REMOVED)
            .build());

        syncBacenCidEvents.syncWithBacen(events);

        assertThat(syncBacenCidEvents.getLastSyncWithBacen()).isEqualTo(eventMostRecent);
    }

}
