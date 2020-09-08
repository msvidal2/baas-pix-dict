package com.picpay.banking.pix.core.validators.addressingkey;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class PersonTypeItemValidator implements DictItemValidator<AddressingKey> {

    @Override
    public void validate(AddressingKey domain) {
        if (domain.getPersonType() == null) {
            throw new IllegalArgumentException("personType can not be empty");
        }
    }
}