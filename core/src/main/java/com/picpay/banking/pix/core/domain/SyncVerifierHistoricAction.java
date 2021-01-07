package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"cid", "actionType"})
public class SyncVerifierHistoricAction {

    private final SyncVerifierHistoric syncVerifierHistoric;
    private final String cid;
    private final ActionType actionType;

    public enum ActionType {
        ADD,
        REMOVE;

        public static ActionType resolve(final ReconciliationAction action) {
            switch (action) {
                case ADDED: return ADD;
                case REMOVED: return REMOVE;
                default: throw new IllegalArgumentException();
            }
        }
    }

}
