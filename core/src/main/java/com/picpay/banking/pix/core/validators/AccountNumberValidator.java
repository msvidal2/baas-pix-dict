package com.picpay.banking.pix.core.validators;

import com.google.common.base.Strings;

public class AccountNumberValidator {

    public static void validate(String value) {
        if (Strings.isNullOrEmpty(value) || value.isBlank()) {
            throw new IllegalArgumentException("Account number can not be empty");
        }

        if (value.length() < 1 || value.length() > 20) {
            throw new IllegalArgumentException("The number of characters in the account number must be between 1 and 20");
        }
    }
}
