package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class SyncVerifierHistoricAction {

    private final SyncVerifierHistoric syncVerifierHistoric;
    private final String cid;
    private final ActionType actionType;
    private final ActionClassification actionClassification;

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

    public enum ActionClassification {
        HAS_IN_BACEN_AND_NOT_HAVE_IN_DATABASE,
        HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN
    }

}
