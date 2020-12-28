package com.picpay.banking.pix.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.BACEN_CID_EVENT_ADD;
import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.BACEN_CID_EVENT_REMOVE;
import static org.assertj.core.api.Assertions.assertThat;

class SyncVerifierHistoricTest {

    @Test
    @DisplayName("Deve agrupar os eventos por cid e pegar a data mais recente")
    void shouldGroupEventsByCidAndGetTheMostRecentDate() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final Set<BacenCidEvent> bacenEvents = Set.of(
            BACEN_CID_EVENT_ADD("1"),
            BACEN_CID_EVENT_REMOVE("1"),
            BACEN_CID_EVENT_ADD("1"),
            BACEN_CID_EVENT_REMOVE("2"),
            BACEN_CID_EVENT_ADD("3"),
            BACEN_CID_EVENT_ADD("4"),
            BACEN_CID_EVENT_ADD("5"),
            BACEN_CID_EVENT_REMOVE("6"),
            BACEN_CID_EVENT_REMOVE("7"),
            BACEN_CID_EVENT_ADD("7"),
            BACEN_CID_EVENT_REMOVE("7"),
            BACEN_CID_EVENT_ADD("7"),
            BACEN_CID_EVENT_REMOVE("7"));

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
