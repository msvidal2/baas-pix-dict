package com.picpay.banking.pix.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.bacenCidEventAdd;
import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.bacenCidEventRemove;
import static org.assertj.core.api.Assertions.assertThat;

class SyncVerifierHistoricTest {

    @Test
    @DisplayName("Deve agrupar os eventos por cid e pegar a data mais recente")
    void shouldGroupEventsByCidAndGetTheMostRecentDate() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final Set<BacenCidEvent> bacenEvents = Set.of(
            bacenCidEventAdd("1"),
            bacenCidEventRemove("1"),
            bacenCidEventAdd("1"),
            bacenCidEventRemove("2"),
            bacenCidEventAdd("3"),
            bacenCidEventAdd("4"),
            bacenCidEventAdd("5"),
            bacenCidEventRemove("6"),
            bacenCidEventRemove("7"),
            bacenCidEventAdd("7"),
            bacenCidEventRemove("7"),
            bacenCidEventAdd("7"),
            bacenCidEventRemove("7"));

        final Set<BacenCidEvent> latestEvents = syncVerifierHistoric.groupBacenEventsByCidMaxByDate(bacenEvents);

        assertThat(latestEvents.size()).isEqualTo(7);
        assertThat(latestEvents.stream()
            .filter(cidEvent -> cidEvent.getCid().equals("1") && cidEvent.getAction().equals(ReconciliationAction.ADDED)).count())
            .isEqualTo(1);
        assertThat(latestEvents.stream()
            .filter(cidEvent -> cidEvent.getCid().equals("2") && cidEvent.getAction().equals(ReconciliationAction.REMOVED)).count())
            .isEqualTo(1);
        assertThat(latestEvents.stream()
            .filter(cidEvent -> cidEvent.getCid().equals("3") && cidEvent.getAction().equals(ReconciliationAction.ADDED)).count())
            .isEqualTo(1);
        assertThat(latestEvents.stream()
            .filter(cidEvent -> cidEvent.getCid().equals("4") && cidEvent.getAction().equals(ReconciliationAction.ADDED)).count())
            .isEqualTo(1);
        assertThat(latestEvents.stream()
            .filter(cidEvent -> cidEvent.getCid().equals("5") && cidEvent.getAction().equals(ReconciliationAction.ADDED)).count())
            .isEqualTo(1);
        assertThat(latestEvents.stream()
            .filter(cidEvent -> cidEvent.getCid().equals("6") && cidEvent.getAction().equals(ReconciliationAction.REMOVED)).count())
            .isEqualTo(1);
        assertThat(latestEvents.stream()
            .filter(cidEvent -> cidEvent.getCid().equals("7") && cidEvent.getAction().equals(ReconciliationAction.REMOVED)).count())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando a não há eventos no bacen")
    void shouldReturnEmptyWhenNoEventsOnBacen() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final Set<BacenCidEvent> bacenEvents = new HashSet<>();

        final Set<BacenCidEvent> differences = syncVerifierHistoric.groupBacenEventsByCidMaxByDate(bacenEvents);

        assertThat(differences.size()).isEqualTo(0);
    }

}
