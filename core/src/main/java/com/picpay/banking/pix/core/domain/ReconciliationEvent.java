package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReconciliationEvent {

    private final String key;
    private final KeyType keyType;
    private final String cid;
    private final ReconciliationAction action;
    private final LocalDateTime keyOwnershipDate;

    public enum KeyType { PHONE, CPF, CNPJ, EMAIL, EVP }

    public Boolean isARemoveAction() { return this.getAction().equals(ReconciliationAction.REMOVE); }
    public Boolean isAUpdateAction() { return this.getAction().equals(ReconciliationAction.UPDATE); }

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
