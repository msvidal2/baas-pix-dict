package com.picpay.banking.pix.core.validators;


import java.time.LocalDateTime;

public class AccountOpeningDateValidator {

    public static void validate(LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("Account opening date cannot be empty");
        }
    }

}
