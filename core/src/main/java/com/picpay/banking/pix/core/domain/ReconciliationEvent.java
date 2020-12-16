package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
public class ReconciliationEvent {

    @NonNull
    private final String key;

    @NonNull
    private final KeyType keyType;

    @NonNull
    private final String cid;

    @NonNull
    private final ReconciliationAction action;

    @NonNull
    private final LocalDateTime eventOnBacenAt;

    public Boolean isARemoveAction() { return this.getAction().equals(ReconciliationAction.REMOVE); }
    public Boolean isAUpdateAction() { return this.getAction().equals(ReconciliationAction.UPDATE); }

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

//    public ContentIdentifierEvent.KeyType getKeyType() throws Exception {
//        switch (this.keyType) {
//            case "PHONE": return ContentIdentifierEvent.KeyType.PHONE;
//            case "CPF": return ContentIdentifierEvent.KeyType.CPF;
//            case "CNPJ": return ContentIdentifierEvent.KeyType.CNPJ;
//            case "EMAIL": return ContentIdentifierEvent.KeyType.EMAIL;
//            case "EVP": return ContentIdentifierEvent.KeyType.EVP;
//            default: throw new Exception(); // TODO: Create a custom specific exception
//        }
//    }
}
