package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@EqualsAndHashCode(of = {"cid", "action", "eventOnBacenAt"})
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

}
