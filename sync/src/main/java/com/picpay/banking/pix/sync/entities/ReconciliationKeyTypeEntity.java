package com.picpay.banking.pix.sync.entities;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ReconciliationKeyTypeEntity {
    CPF(ReconciliationEvent.KeyType.CPF),
    CNPJ(ReconciliationEvent.KeyType.CNPJ),
    PHONE(ReconciliationEvent.KeyType.PHONE),
    EMAIL(ReconciliationEvent.KeyType.EMAIL),
    EVP(ReconciliationEvent.KeyType.EVP);

    private final ReconciliationEvent.KeyType keyType;

    public static ReconciliationKeyTypeEntity resolve(ReconciliationEvent.KeyType keyType) {
        return Arrays.stream(ReconciliationKeyTypeEntity.values())
                .filter(type -> type.keyType.equals(keyType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
