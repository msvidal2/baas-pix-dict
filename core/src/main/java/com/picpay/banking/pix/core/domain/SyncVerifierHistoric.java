package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
public class SyncVerifierHistoric {

    private final KeyType keyType;
    private final String vsyncStart;
    private final String vsyncEnd;
    private final LocalDateTime synchronizedStart;
    private final LocalDateTime synchronizedEnd;
    private final SyncVerifierResult syncVerifierResult;

    public Set<ContentIdentifierAction> identifyActions(final List<ContentIdentifierEvent> bacenEvents,
        final List<ContentIdentifierEvent> databaseEvents) {

        var bacenLatestActions = groupByCidMaxByDateAndMapToActions(bacenEvents);
        var databaseLatestActions = groupByCidMaxByDateAndMapToActions(databaseEvents);

        var resultActions = new HashSet<ContentIdentifierAction>();
        resultActions.addAll(hasInBacenAndNotHaveInDatabase(bacenLatestActions, databaseLatestActions));
        resultActions.addAll(hasInDatabaseAndNotHaveInBacen(databaseLatestActions, bacenLatestActions));

        return resultActions;
    }

    private Set<ContentIdentifierAction> hasInDatabaseAndNotHaveInBacen(final Set<ContentIdentifierAction> databaseLatestContentIdentifierActions,
        final Set<ContentIdentifierAction> bacenLatestContentIdentifierActions) {
        var resultActions = new HashSet<ContentIdentifierAction>();
        for (final ContentIdentifierAction databaseContentIdentifierAction : databaseLatestContentIdentifierActions) {
            var databaseCidExistsInBacenCid = bacenLatestContentIdentifierActions.stream()
                .anyMatch(contentIdentifier -> contentIdentifier.getCid().equals(databaseContentIdentifierAction.getCid()));

            if (!databaseCidExistsInBacenCid) {
                resultActions.add(new ContentIdentifierAction(databaseContentIdentifierAction.getCid(), ActionType.REMOVE));
            }
        }

        return resultActions;
    }

    private Set<ContentIdentifierAction> hasInBacenAndNotHaveInDatabase(final Set<ContentIdentifierAction> bacenLatestContentIdentifierActions,
        final Set<ContentIdentifierAction> databaseLatestContentIdentifierActions) {
        var resultActions = new HashSet<ContentIdentifierAction>();
        for (ContentIdentifierAction bacenContentIdentifierAction : bacenLatestContentIdentifierActions) {
            if (!databaseLatestContentIdentifierActions.contains(bacenContentIdentifierAction)) {
                resultActions.add(bacenContentIdentifierAction);
            }
        }

        return resultActions;
    }

    private Set<ContentIdentifierAction> groupByCidMaxByDateAndMapToActions(final List<ContentIdentifierEvent> bacenEvents) {
        return new HashSet<>(bacenEvents.stream().collect(
            Collectors.groupingBy(ContentIdentifierEvent::getCid,
                Collectors.maxBy(Comparator.comparing(ContentIdentifierEvent::getKeyOwnershipDate))))
            .values()).stream().map(
            event -> new ContentIdentifierAction(event.orElseThrow().getCid(), ActionType.resolve(event.get().getContentIdentifierType())))
            .collect(Collectors.toSet());
    }

    public enum ActionType {
        ADD,
        REMOVE;

        public static ActionType resolve(final ContentIdentifierEvent.ContentIdentifierEventType eventType) {
            if (eventType.equals(ContentIdentifierEvent.ContentIdentifierEventType.ADD)) return ADD;
            return REMOVE;
        }
    }

}
