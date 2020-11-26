package com.picpay.banking.pix.core.validators;


import com.picpay.banking.pix.core.domain.PersonType;

public class PersonTypeValidator {

    public static void validate(PersonType value) {
        if (value == null) {
            throw new IllegalArgumentException("Person type cannot be null");
        }
    }

}