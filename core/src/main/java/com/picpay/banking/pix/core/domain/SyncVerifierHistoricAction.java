package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class SyncVerifierHistoricAction {

    private final SyncVerifierHistoric syncVerifierHistoric;
    private final String cid;
    private final ActionType actionType;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SyncVerifierHistoricAction syncVerifierHistoricAction = (SyncVerifierHistoricAction) o;
        return Objects.equals(cid, syncVerifierHistoricAction.cid) &&
            actionType == syncVerifierHistoricAction.actionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid, actionType);
    }

    public enum ActionType {
        ADD,
        REMOVE;

        public static ActionType resolve(final ReconciliationAction action) {
            switch (action) {
                case ADD: return ADD;
                case REMOVE: return REMOVE;
                default: throw new IllegalArgumentException();
            }
        }
    }

}
