package com.picpay.banking.pix.core.validators;


import com.picpay.banking.pix.core.domain.AccountType;

public class AccountTypeValidator {

    public static void validate(AccountType value) {
        if (value == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
    }

}
