package com.picpay.banking.pixkey.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum KeyType {

    CPF(com.picpay.banking.pix.core.domain.KeyType.CPF),
    CNPJ(com.picpay.banking.pix.core.domain.KeyType.CNPJ),
    PHONE(com.picpay.banking.pix.core.domain.KeyType.CELLPHONE),
    EMAIL(com.picpay.banking.pix.core.domain.KeyType.EMAIL),
    EVP(com.picpay.banking.pix.core.domain.KeyType.RANDOM);

    private com.picpay.banking.pix.core.domain.KeyType type;

    public static KeyType resolve(com.picpay.banking.pix.core.domain.KeyType type) {
        return Arrays.stream(KeyType.values())
                .filter(keyType -> keyType.type.equals(type))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}
