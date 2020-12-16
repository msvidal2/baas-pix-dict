package com.picpay.banking.pix.core.validators;

import com.google.common.base.Strings;

public class TaxIdValidator {

    public static void validate(String value) {
        if (Strings.isNullOrEmpty(value) || value.isBlank()) {
            throw new IllegalArgumentException("CPF/CNPJ cannot be empty");
        }

        if (value.length() > 14) {
            throw new IllegalArgumentException("The number of characters in the CPF/CNPJ must be less than or equals to 14");
        }
    }

}
