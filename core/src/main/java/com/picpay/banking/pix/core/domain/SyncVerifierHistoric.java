package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
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

    public Set<BacenCidEvent> groupBacenEventsByCidMaxByDate(final Set<BacenCidEvent> bacenEvents) {
        if (bacenEvents.isEmpty()) return new HashSet<>();

        return bacenEvents.stream()
            .collect(Collectors.groupingBy(BacenCidEvent::getCid, Collectors.maxBy(Comparator.comparing(BacenCidEvent::getEventOnBacenAt))))
            .values().stream().map(Optional::orElseThrow).collect(Collectors.toSet());
    }

    public boolean isNOK() {
        return syncVerifierResultType.equals(SyncVerifierResultType.NOK);
    }

    public boolean isOK() {
        return syncVerifierResultType.equals(SyncVerifierResultType.OK);
    }

}
