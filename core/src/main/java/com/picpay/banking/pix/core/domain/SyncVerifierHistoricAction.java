package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class SyncVerifierHistoricAction {

    private SyncVerifierHistoric syncVerifierHistoric;
    private String cid;
    private ActionType actionType;

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

        public static ActionType resolve(final ContentIdentifierEvent.ContentIdentifierEventType eventType) {
            if (eventType.equals(ContentIdentifierEvent.ContentIdentifierEventType.ADDED)) return ADD;
            return REMOVE;
        }
    }

}
