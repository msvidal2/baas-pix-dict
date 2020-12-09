package com.picpay.banking.pixkey.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum KeyTypeBacen {

    CPF(com.picpay.banking.pix.core.domain.KeyType.CPF, 0),
    CNPJ(com.picpay.banking.pix.core.domain.KeyType.CNPJ, 1),
    EMAIL(com.picpay.banking.pix.core.domain.KeyType.EMAIL, 2),
    PHONE(com.picpay.banking.pix.core.domain.KeyType.CELLPHONE, 3),
    EVP(com.picpay.banking.pix.core.domain.KeyType.RANDOM, 4);

    private com.picpay.banking.pix.core.domain.KeyType type;
    private int value;

    public static KeyTypeBacen resolve(com.picpay.banking.pix.core.domain.KeyType type) {
        return Arrays.stream(KeyTypeBacen.values())
                .filter(keyType -> keyType.type.equals(type))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}
