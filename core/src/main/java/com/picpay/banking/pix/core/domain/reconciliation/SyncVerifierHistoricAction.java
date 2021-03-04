package com.picpay.banking.pix.core.domain.reconciliation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"cid", "action"})
public class SyncVerifierHistoricAction {

    private final SyncVerifierHistoric syncVerifierHistoric;
    private final String cid;
    private final ReconciliationAction action;

}
