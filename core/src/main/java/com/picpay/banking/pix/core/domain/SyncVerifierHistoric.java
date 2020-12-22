package com.picpay.banking.pix.core.domain;

import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Set<BacenCidEvent> bacenEvents;
    private Set<ReconciliationEvent> databaseEvents;

    public Set<SyncVerifierHistoricDifference> identifyDifferences(final Set<BacenCidEvent> bacenEvents,
        final Set<ReconciliationEvent> databaseEvents) {
        this.bacenEvents = bacenEvents;
        this.databaseEvents = databaseEvents;

        var bacenLatestDiferences = groupBacenEventsByCidMaxByDateAndMapToDifferences(bacenEvents);
        var databaseLatestDifferences = groupDatabaseEventsByCidMaxByDateAndMapToDifferences(databaseEvents);

        var differences = new HashSet<SyncVerifierHistoricDifference>();
        differences.addAll(Sets.difference(bacenLatestDiferences, databaseLatestDifferences));
        differences.addAll(Sets.difference(databaseLatestDifferences, bacenLatestDiferences));

        return differences;
    }

    private Set<SyncVerifierHistoricDifference> groupDatabaseEventsByCidMaxByDateAndMapToDifferences(final Set<ReconciliationEvent> bacenEvents) {
        return new HashSet<>(bacenEvents.stream().collect(
            Collectors.groupingBy(ReconciliationEvent::getCid, Collectors.maxBy(Comparator.comparing(ReconciliationEvent::getEventOnBacenAt))))
            .values()).stream().map(event -> SyncVerifierHistoricDifference.from(this, event.orElseThrow()))
            .collect(Collectors.toSet());
    }

    private Set<SyncVerifierHistoricDifference> groupBacenEventsByCidMaxByDateAndMapToDifferences(final Set<BacenCidEvent> bacenEvents) {
        return new HashSet<>(bacenEvents.stream()
            .collect(Collectors.groupingBy(BacenCidEvent::getCid, Collectors.maxBy(Comparator.comparing(BacenCidEvent::getEventOnBacenAt))))
            .values()).stream().map(event -> SyncVerifierHistoricDifference.from(this, event.orElseThrow()))
            .collect(Collectors.toSet());
    }

    public boolean isNOK() {
        return syncVerifierResultType.equals(SyncVerifierResultType.NOK);
    }

    public boolean isOK() {
        return syncVerifierResultType.equals(SyncVerifierResultType.OK);
    }

}
