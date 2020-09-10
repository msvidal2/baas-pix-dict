package com.picpay.banking.pix.core.domain;

import com.picpay.banking.pix.core.validators.key.CNPJKeyValidator;
import com.picpay.banking.pix.core.validators.key.CPFKeyValidator;
import com.picpay.banking.pix.core.validators.key.CellPhoneKeyValidator;
import com.picpay.banking.pix.core.validators.key.RandomKeyValidator;
import com.picpay.banking.pix.core.validators.key.EmailKeyValidator;
import com.picpay.banking.pix.core.validators.key.KeyValidator;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeyType {

    CPF(0, new CPFKeyValidator()),
    CNPJ(1, new CNPJKeyValidator()),
    EMAIL(2, new EmailKeyValidator()),
    CELLPHONE(3, new CellPhoneKeyValidator()),
    RANDOM(4, new RandomKeyValidator());

    private int value;

    private KeyValidator validator;

    public static KeyType resolve(int value) {
        for(KeyType keyType : values()) {
            if (keyType.value == value) {
                return keyType;
            }
        }

        return null;
    }

    public static KeyType resolveByValue(String value) {
        for(KeyType keyType : values()) {
            try {
                keyType.validator.validate(value);

                return keyType;
            } catch (KeyValidatorException e) {}
        }

        return null;
    }

}
