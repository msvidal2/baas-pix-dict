package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
public class ReconciliationEvent {


    private final String key;

    private final KeyType keyType;

    @NonNull
    private final String cid;

    @NonNull
    private final ReconciliationAction action;

    @NonNull
    private final LocalDateTime eventOnBacenAt;

    public Boolean isARemoveAction() {
        return this.getAction().equals(ReconciliationAction.REMOVED);
    }

    public Boolean isAUpdateAction() {
        return this.getAction().equals(ReconciliationAction.UPDATED);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReconciliationEvent that = (ReconciliationEvent) o;
        return cid.equals(that.cid) && action == that.action && eventOnBacenAt.equals(that.eventOnBacenAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid, action, eventOnBacenAt);
    }

}
