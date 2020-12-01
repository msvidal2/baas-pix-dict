package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class ContentIdentifierAction {

    private String cid;
    private SyncVerifierHistoric.ActionType actionType;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ContentIdentifierAction contentIdentifierAction = (ContentIdentifierAction) o;
        return Objects.equals(cid, contentIdentifierAction.cid) &&
            actionType == contentIdentifierAction.actionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid, actionType);
    }

}
