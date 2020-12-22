package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction.ActionClassification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.BACEN_CID_EVENT_ADD;
import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.BACEN_CID_EVENT_REMOVE;
import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.DATABASE_CID_EVENT_ADD;
import static com.picpay.banking.pix.core.util.ContentIdentifierUtil.DATABASE_CID_EVENT_REMOVE;
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
            BACEN_CID_EVENT_REMOVE("6"));

        final Set<SyncVerifierHistoricDifference> differences = syncVerifierHistoric.identifyDifferences(bacenEvents, new HashSet<>());

        assertThat(differences.size()).isEqualTo(6);
        assertThat(differences.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getActionClassification().equals(
                ActionClassification.HAS_IN_BACEN_AND_NOT_HAVE_IN_DATABASE)).count()).isEqualTo(6);
        assertThat(differences.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getActionClassification().equals(
                ActionClassification.HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN)).count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Quando tem eventos no database e não tem no Bacen todos devem estar classificados como HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN")
    void should_classify_HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final Set<ReconciliationEvent> databaseEvents = Set.of(
            DATABASE_CID_EVENT_ADD("1"),
            DATABASE_CID_EVENT_ADD("2"),
            DATABASE_CID_EVENT_ADD("3"),
            DATABASE_CID_EVENT_ADD("4"),
            DATABASE_CID_EVENT_ADD("5"));

        final Set<SyncVerifierHistoricDifference> syncVerifierHistoricActions = syncVerifierHistoric.identifyDifferences(new HashSet<>(),
            databaseEvents);

        assertThat(syncVerifierHistoricActions.size()).isEqualTo(5);
        assertThat(syncVerifierHistoricActions.stream().filter(
            contentIdentifierAction -> contentIdentifierAction.getActionClassification().equals(
                ActionClassification.HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN)).count()).isEqualTo(5);
    }

    @Test
    @DisplayName("Apenas os CIDs 2 e 6 devem compor as diferenças")
    void onlyCID2And6ShouldCompareTheDifferences() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final Set<BacenCidEvent> bacenEvents = Set.of(
            BACEN_CID_EVENT_ADD("1"),
            BACEN_CID_EVENT_REMOVE("1"),
            BACEN_CID_EVENT_ADD("1"),
            BACEN_CID_EVENT_REMOVE("2"),
            BACEN_CID_EVENT_ADD("3"),
            BACEN_CID_EVENT_ADD("4"),
            BACEN_CID_EVENT_ADD("5"),
            BACEN_CID_EVENT_REMOVE("6"));

        final Set<ReconciliationEvent> databaseEvents = Set.of(
            DATABASE_CID_EVENT_ADD("1"),
            DATABASE_CID_EVENT_ADD("2"),
            DATABASE_CID_EVENT_ADD("3"),
            DATABASE_CID_EVENT_ADD("4"),
            DATABASE_CID_EVENT_ADD("5"),
            DATABASE_CID_EVENT_ADD("6"));

        final Set<SyncVerifierHistoricDifference> differences = syncVerifierHistoric.identifyDifferences(bacenEvents, databaseEvents);

        var hasInBacenAndNotHaveInDatabase = differences.stream().filter(
            difference -> ActionClassification.HAS_IN_BACEN_AND_NOT_HAVE_IN_DATABASE.equals(difference.getActionClassification()))
            .collect(Collectors.toSet());

        var hasInDatabaseAndNotHaveInBacen = differences.stream().filter(
            difference -> ActionClassification.HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN.equals(difference.getActionClassification()))
            .collect(Collectors.toSet());

        assertThat(hasInBacenAndNotHaveInDatabase.size()).isEqualTo(2);
        assertThat(hasInDatabaseAndNotHaveInBacen.size()).isEqualTo(2);
        assertThat(hasInBacenAndNotHaveInDatabase.stream().filter(difference -> difference.getCid().equals("2")).count()).isEqualTo(1);
        assertThat(hasInBacenAndNotHaveInDatabase.stream().filter(difference -> difference.getCid().equals("6")).count()).isEqualTo(1);
        assertThat(hasInDatabaseAndNotHaveInBacen.stream().filter(difference -> difference.getCid().equals("2")).count()).isEqualTo(1);
        assertThat(hasInDatabaseAndNotHaveInBacen.stream().filter(difference -> difference.getCid().equals("6")).count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve encontrar 11 diferenças, sendo 5 hasInBacenAndNotHaveInDatabase e 6 hasInDatabaseAndNotHaveInBacen")
    void should_generate_11_differences() {
        var syncVerifierHistoric = SyncVerifierHistoric.builder().build();

        final Set<BacenCidEvent> bacenCids = Set.of(
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
            BACEN_CID_EVENT_REMOVE("8"),
            BACEN_CID_EVENT_ADD("8"),
            BACEN_CID_EVENT_REMOVE("8"));

        final Set<ReconciliationEvent> databaseCids = Set.of(
            DATABASE_CID_EVENT_ADD("1"),
            DATABASE_CID_EVENT_REMOVE("1"),
            DATABASE_CID_EVENT_ADD("2"),
            DATABASE_CID_EVENT_ADD("4"),
            DATABASE_CID_EVENT_ADD("5"),
            DATABASE_CID_EVENT_ADD("6"),
            DATABASE_CID_EVENT_REMOVE("7"),
            DATABASE_CID_EVENT_REMOVE("8"),
            DATABASE_CID_EVENT_REMOVE("9"),
            DATABASE_CID_EVENT_ADD("10"));

        final Set<SyncVerifierHistoricDifference> differences = syncVerifierHistoric.identifyDifferences(bacenCids, databaseCids);

        var hasInBacenAndNotHaveInDatabase = differences.stream().filter(
            difference -> ActionClassification.HAS_IN_BACEN_AND_NOT_HAVE_IN_DATABASE.equals(difference.getActionClassification()))
            .collect(Collectors.toSet());

        var hasInDatabaseAndNotHaveInBacen = differences.stream().filter(
            difference -> ActionClassification.HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN.equals(difference.getActionClassification()))
            .collect(Collectors.toSet());

        assertThat(differences.size()).isEqualTo(11);
        assertThat(hasInBacenAndNotHaveInDatabase.size()).isEqualTo(5);
        assertThat(hasInDatabaseAndNotHaveInBacen.size()).isEqualTo(6);

        assertThat(hasInBacenAndNotHaveInDatabase.stream().filter(difference ->
            difference.getCid().equals("1") && ReconciliationAction.ADDED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
        assertThat(hasInBacenAndNotHaveInDatabase.stream().filter(difference ->
            difference.getCid().equals("3") && ReconciliationAction.ADDED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
        assertThat(hasInBacenAndNotHaveInDatabase.stream().filter(difference ->
            difference.getCid().equals("7") && ReconciliationAction.ADDED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
        assertThat(hasInBacenAndNotHaveInDatabase.stream().filter(difference ->
            difference.getCid().equals("2") && ReconciliationAction.REMOVED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
        assertThat(hasInBacenAndNotHaveInDatabase.stream().filter(difference ->
            difference.getCid().equals("6") && ReconciliationAction.REMOVED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);

        assertThat(hasInDatabaseAndNotHaveInBacen.stream().filter(difference ->
            difference.getCid().equals("2") && ReconciliationAction.ADDED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
        assertThat(hasInDatabaseAndNotHaveInBacen.stream().filter(difference ->
            difference.getCid().equals("6") && ReconciliationAction.ADDED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
        assertThat(hasInDatabaseAndNotHaveInBacen.stream().filter(difference ->
            difference.getCid().equals("10") && ReconciliationAction.ADDED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
        assertThat(hasInDatabaseAndNotHaveInBacen.stream().filter(difference ->
            difference.getCid().equals("1") && ReconciliationAction.REMOVED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
        assertThat(hasInDatabaseAndNotHaveInBacen.stream().filter(difference ->
            difference.getCid().equals("7") && ReconciliationAction.REMOVED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
        assertThat(hasInDatabaseAndNotHaveInBacen.stream().filter(difference ->
            difference.getCid().equals("9") && ReconciliationAction.REMOVED.equals(difference.getReconciliationAction())).count()).isEqualTo(1);
    }

}
