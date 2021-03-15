package com.picpay.banking.pix.core.domain.reconciliation;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class SyncVerifierHistoric {

    private final Integer id;
    private final KeyType keyType;
    private final String vsyncStart;
    private final String vsyncEnd;
    private final LocalDateTime synchronizedStart;
    private final LocalDateTime synchronizedEnd;
    private final SyncVerifierResultType syncVerifierResultType;
    @Builder.Default
    private ReconciliationMethod reconciliationMethod = ReconciliationMethod.ONLY_VERIFIER;

    public Set<BacenCidEvent> groupBacenEventsByCidMaxByDate(final Collection<BacenCidEvent> bacenEvents) {
        if (bacenEvents.isEmpty()) {
            return new HashSet<>();
        }

        return bacenEvents.stream()
            .collect(Collectors.groupingBy(BacenCidEvent::getCid, Collectors.maxBy(Comparator.comparing(BacenCidEvent::getEventOnBacenAt))))
            .values().stream().map(Optional::orElseThrow).collect(Collectors.toSet());
    }

    public boolean isNOK() {
        return syncVerifierResultType == SyncVerifierResultType.NOK;
    }

    public boolean isOK() {
        return syncVerifierResultType == SyncVerifierResultType.OK;
    }

    public void fixedByAggregateReconciliation() {
        this.reconciliationMethod = ReconciliationMethod.FIX_BY_AGGREGATE;
    }

    public void fixedByFileReconciliation() {
        this.reconciliationMethod = ReconciliationMethod.FIX_BY_FILE;
    }

    public enum ReconciliationMethod {
        ONLY_VERIFIER, FIX_BY_AGGREGATE, FIX_BY_FILE;
    }

}
