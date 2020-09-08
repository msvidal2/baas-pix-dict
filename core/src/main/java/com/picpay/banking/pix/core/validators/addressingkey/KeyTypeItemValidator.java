package com.picpay.banking.pix.core.validators.addressingkey;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class KeyTypeItemValidator implements DictItemValidator<AddressingKey> {

    @Override
    public void validate(AddressingKey domain) {
        if (domain.getType() == null) {
            throw new IllegalArgumentException("type can not be empty");
        }
    }
}
