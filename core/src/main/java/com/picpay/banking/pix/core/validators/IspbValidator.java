package com.picpay.banking.pix.core.validators;


public class IspbValidator {

    public static void validate(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("ISPB cannot be empty");
        }

        if(value == 0) {
            throw new IllegalArgumentException("Invalid ISPB");
        }

        if (String.valueOf(value).length() > 8) {
            throw new IllegalArgumentException("The number of characters in the ISPB must be less than 8");
        }
    }

}
