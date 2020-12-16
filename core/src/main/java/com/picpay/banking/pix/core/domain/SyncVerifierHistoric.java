package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction.ActionType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction.ActionClassification;

@Builder
@Getter
@ToString
public class SyncVerifierHistoric {

    private final Integer id;
    private final KeyType keyType;
    private final String vsyncStart;
    private final String vsyncEnd;
    private final LocalDateTime synchronizedStart;
    private final LocalDateTime synchronizedEnd;
    private final SyncVerifierResultType syncVerifierResultType;

    public Set<SyncVerifierHistoricAction> identifyActions(final Set<ReconciliationEvent> bacenEvents,
        final Set<ReconciliationEvent> databaseEvents) {

        var bacenLatestActions = groupByCidMaxByDateAndMapToActions(bacenEvents);
        var databaseLatestActions = groupByCidMaxByDateAndMapToActions(databaseEvents);

        var resultActions = new HashSet<SyncVerifierHistoricAction>();
        resultActions.addAll(hasInBacenAndNotHaveInDatabase(bacenLatestActions, databaseLatestActions));
        resultActions.addAll(hasInDatabaseAndNotHaveInBacen(databaseLatestActions, bacenLatestActions));

        return resultActions;
    }

    private Set<SyncVerifierHistoricAction> hasInDatabaseAndNotHaveInBacen(
        final Set<SyncVerifierHistoricAction> databaseLatestSyncVerifierHistoricActions,
        final Set<SyncVerifierHistoricAction> bacenLatestSyncVerifierHistoricActions) {
        var resultActions = new HashSet<SyncVerifierHistoricAction>();
        for (final SyncVerifierHistoricAction databaseSyncVerifierHistoricAction : databaseLatestSyncVerifierHistoricActions) {
            var databaseCidExistsInBacenCid = bacenLatestSyncVerifierHistoricActions.stream()
                .anyMatch(contentIdentifier -> contentIdentifier.getCid().equals(databaseSyncVerifierHistoricAction.getCid()));

            if (!databaseCidExistsInBacenCid) {
                resultActions.add(
                    new SyncVerifierHistoricAction(this,
                        databaseSyncVerifierHistoricAction.getCid(),
                        ActionType.REMOVE, ActionClassification.HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN));
            }
        }

        return resultActions;
    }

    private Set<SyncVerifierHistoricAction> hasInBacenAndNotHaveInDatabase(
        final Set<SyncVerifierHistoricAction> bacenLatestSyncVerifierHistoricActions,
        final Set<SyncVerifierHistoricAction> databaseLatestSyncVerifierHistoricActions) {

        var resultActions = new HashSet<SyncVerifierHistoricAction>();
        for (SyncVerifierHistoricAction bacenSyncVerifierHistoricAction : bacenLatestSyncVerifierHistoricActions) {
            if (!databaseLatestSyncVerifierHistoricActions.contains(bacenSyncVerifierHistoricAction)) {
                resultActions.add(bacenSyncVerifierHistoricAction);
            }
        }

        return resultActions;
    }

    private Set<SyncVerifierHistoricAction> groupByCidMaxByDateAndMapToActions(final Set<ReconciliationEvent> bacenEvents) {
        return new HashSet<>(bacenEvents.stream().collect(
            Collectors.groupingBy(ReconciliationEvent::getCid,
                Collectors.maxBy(Comparator.comparing(ReconciliationEvent::getEventOnBacenAt))))
            .values()).stream().map(
            event -> new SyncVerifierHistoricAction(this, event.orElseThrow().getCid(),
                ActionType.resolve(event.get().getContentIdentifierType()),
                ActionClassification.HAS_IN_BACEN_AND_NOT_HAVE_IN_DATABASE))
            .collect(Collectors.toSet());
    }

    public boolean isNOK() {
        return syncVerifierResultType.equals(SyncVerifierResultType.NOK);
    }

    public boolean isOK() {
        return syncVerifierResultType.equals(SyncVerifierResultType.OK);
    }

}
