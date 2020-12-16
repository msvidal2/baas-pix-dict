package com.picpay.banking.pix.core.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"cid", "actionType"})
public class SyncVerifierHistoricAction {

    private final SyncVerifierHistoric syncVerifierHistoric;
    private final String cid;
    private final ActionType actionType;
    private final ActionClassification actionClassification;

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

    public enum ActionClassification {
        HAS_IN_BACEN_AND_NOT_HAVE_IN_DATABASE,
        HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN
    }

}
