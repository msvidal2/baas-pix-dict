package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.validators.key.CNPJKeyValidator;
import com.picpay.banking.pix.core.validators.key.CPFKeyValidator;
import com.picpay.banking.pix.core.validators.key.CellPhoneKeyValidator;
import com.picpay.banking.pix.core.validators.key.EmailKeyValidator;
import com.picpay.banking.pix.core.validators.key.KeyValidator;
import com.picpay.banking.pix.core.validators.key.RandomKeyValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum KeyType {

    CPF  (0, new CPFKeyValidator()),
    CNPJ (1, new CNPJKeyValidator()),
    EMAIL(2, new EmailKeyValidator()),
    CELLPHONE(3, new CellPhoneKeyValidator()),
    RANDOM  (4, new RandomKeyValidator());

    private int value;

    private KeyValidator<String> validator;

    public static KeyType resolve(int value) {
        for(KeyType keyType : values()) {
            if (keyType.value == value) {
                return keyType;
            }
        }

        return null;
    }


}
