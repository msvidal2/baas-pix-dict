package com.picpay.banking.pix.core.validators;

public class BranchNumberValidator  {

    public static void validate(String value) {
        if (value == null) {
            return;
        }

        if (value.isEmpty() || value.isBlank()) {
            throw new IllegalArgumentException("branch number can not be empty");
        }

        if (value.trim().length() < 1  || value.trim().length() > 4) {
            throw new IllegalArgumentException("The number of characters in the branch number must be between 1 and 4");
        }
    }

}
