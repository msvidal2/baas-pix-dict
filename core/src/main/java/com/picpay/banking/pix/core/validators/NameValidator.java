package com.picpay.banking.pix.core.validators;

import com.google.common.base.Strings;

public class NameValidator {

    public static void validate(String value) {
        if (Strings.isNullOrEmpty(value) || value.isBlank()) {
            throw new IllegalArgumentException("Name can not be empty");
        }

        if (value.length() > 100) {
            throw new IllegalArgumentException("The number of characters in the name must be less than 100");
        }
    }

}
