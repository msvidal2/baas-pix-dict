package com.picpay.banking.pix.original.dto;

import com.picpay.banking.pix.core.domain.KeyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum KeyTypeOriginal {

    CPF(KeyType.CPF),
    CNPJ(KeyType.CNPJ),
    EMAIL(KeyType.EMAIL),
    PHONE(KeyType.CELLPHONE),
    EVP(KeyType.RANDOM);

    private KeyType keyType;

    public static KeyTypeOriginal resolveFromDomain(KeyType keyType) {
        return Stream.of(values())
                .filter(v -> v.keyType.equals(keyType))
                .findAny()
                .orElse(null);
    }

}
