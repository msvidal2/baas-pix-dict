package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = {"cid", "reconciliationAction"})
public class SyncVerifierHistoricDifference {

    private final SyncVerifierHistoric syncVerifierHistoric;
    private final String cid;
    private final ReconciliationAction reconciliationAction;
    private final SyncVerifierHistoricAction.ActionClassification actionClassification;

    public static SyncVerifierHistoricDifference from(SyncVerifierHistoric syncVerifierHistoric, ReconciliationEvent reconciliationEvent) {
        return SyncVerifierHistoricDifference.builder()
            .syncVerifierHistoric(syncVerifierHistoric)
            .cid(reconciliationEvent.getCid())
            .reconciliationAction(reconciliationEvent.getAction())
            .actionClassification(SyncVerifierHistoricAction.ActionClassification.HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN)
            .build();
    }

    public static SyncVerifierHistoricDifference from(SyncVerifierHistoric syncVerifierHistoric, BacenCidEvent bacenCidEvent) {
        return SyncVerifierHistoricDifference.builder()
            .syncVerifierHistoric(syncVerifierHistoric)
            .cid(bacenCidEvent.getCid())
            .reconciliationAction(bacenCidEvent.getAction())
            .actionClassification(SyncVerifierHistoricAction.ActionClassification.HAS_IN_BACEN_AND_NOT_HAVE_IN_DATABASE)
            .build();
    }

}
