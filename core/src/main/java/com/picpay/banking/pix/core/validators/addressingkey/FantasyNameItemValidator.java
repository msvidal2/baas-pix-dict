package com.picpay.banking.pix.core.validators.addressingkey;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class FantasyNameItemValidator implements DictItemValidator<AddressingKey> {

    @Override
    public void validate(AddressingKey domain) {
        if (domain.getFantasyName() == null) {
            return;
        }

        if (domain.getFantasyName().length() > 100) {
            throw new IllegalArgumentException("The number of characters in the fantasyName must be less than 100");
        }
    }
}
