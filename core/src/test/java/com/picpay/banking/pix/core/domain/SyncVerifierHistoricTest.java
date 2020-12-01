package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoric.ActionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.createContentIdentifier;
import static org.assertj.core.api.Assertions.assertThat;

class SyncVerifierHistoricTest {

    @Test
    @DisplayName("Deve agrupar os eventos por cid e pegar a data mais recente")
    void should_generate_4_actions_add_and_2_remove() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final List<ContentIdentifierEvent> bacenEvents = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("1", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("1"),
            createContentIdentifier("2", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE));

        final Set<ContentIdentifierAction> contentIdentifierActions = syncVerifierHistoric.identifyActions(bacenEvents, new ArrayList<>());

        assertThat(contentIdentifierActions.size()).isEqualTo(6);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getActionType().equals(ActionType.ADD)).count()).isEqualTo(4);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getActionType().equals(ActionType.REMOVE)).count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Quando não tem eventos no Bacen, mas tem CIDs, todos devem gerar ações de REMOVE")
    void should_generate_5_actions_remove() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final List<ContentIdentifierEvent> databaseEvents = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("2"),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"));

        final Set<ContentIdentifierAction> contentIdentifierActions = syncVerifierHistoric.identifyActions(new ArrayList<>(), databaseEvents);

        assertThat(contentIdentifierActions.size()).isEqualTo(5);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getActionType().equals(ActionType.REMOVE)).count()).isEqualTo(5);
    }

    @Test
    @DisplayName("Apenas os CIDs 2 e 6 devem gerar ações para REMOVE.")
    void should_generate_action_to_remove_for_2_and_6() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final List<ContentIdentifierEvent> bacenEvents = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("1", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("1"),
            createContentIdentifier("2", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE));

        final List<ContentIdentifierEvent> databaseEvents = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("2"),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6"));

        final Set<ContentIdentifierAction> contentIdentifierActions = syncVerifierHistoric.identifyActions(bacenEvents, databaseEvents);

        assertThat(contentIdentifierActions.size()).isEqualTo(2);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getCid().equals("2")).findFirst().orElseThrow().getActionType())
            .isEqualTo(ActionType.REMOVE);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getCid().equals("6")).findFirst().orElseThrow().getActionType())
            .isEqualTo(ActionType.REMOVE);
    }

    @Test
    @DisplayName("Deve gerar 7 ações. 3 Add e 4 Remove")
    void should_generate_7_actions() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final List<ContentIdentifierEvent> bacenCids = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("1", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("1"),
            createContentIdentifier("2", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("3"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("7", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("7"),
            createContentIdentifier("8", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("8"),
            createContentIdentifier("8", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE));

        final List<ContentIdentifierEvent> databaseCids = List.of(
            createContentIdentifier("1"),
            createContentIdentifier("1", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("2"),
            createContentIdentifier("4"),
            createContentIdentifier("5"),
            createContentIdentifier("6"),
            createContentIdentifier("7", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("8", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("9", ContentIdentifierEvent.ContentIdentifierEventType.REMOVE),
            createContentIdentifier("10"));

        final Set<ContentIdentifierAction> contentIdentifierActions = syncVerifierHistoric.identifyActions(bacenCids, databaseCids);

        assertThat(contentIdentifierActions.size()).isEqualTo(7);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getCid().equals("1")).findFirst().orElseThrow().getActionType())
            .isEqualTo(ActionType.ADD);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getCid().equals("2")).findFirst().orElseThrow().getActionType())
            .isEqualTo(ActionType.REMOVE);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getCid().equals("3")).findFirst().orElseThrow().getActionType())
            .isEqualTo(ActionType.ADD);
        assertThat(contentIdentifierActions.stream().filter(contentIdentifierAction -> contentIdentifierAction.getCid().equals("4")).count())
            .isZero();
        assertThat(contentIdentifierActions.stream().filter(contentIdentifierAction -> contentIdentifierAction.getCid().equals("5")).count())
            .isZero();
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getCid().equals("6")).findFirst().orElseThrow().getActionType())
            .isEqualTo(ActionType.REMOVE);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getCid().equals("7")).findFirst().orElseThrow().getActionType())
            .isEqualTo(ActionType.ADD);
        assertThat(contentIdentifierActions.stream().filter(contentIdentifierAction -> contentIdentifierAction.getCid().equals("8")).count())
            .isZero();
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getCid().equals("9")).findFirst().orElseThrow().getActionType())
            .isEqualTo(ActionType.REMOVE);
        assertThat(contentIdentifierActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getCid().equals("10")).findFirst().orElseThrow().getActionType())
            .isEqualTo(ActionType.REMOVE);
    }

}
