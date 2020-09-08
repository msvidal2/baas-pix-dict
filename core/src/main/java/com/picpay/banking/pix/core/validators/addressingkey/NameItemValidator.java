package com.picpay.banking.pix.core.validators.addressingkey;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class NameItemValidator implements DictItemValidator<AddressingKey> {

    @Override
    public void validate(AddressingKey domain) {
        if (Strings.isNullOrEmpty(domain.getName())) {
            throw new IllegalArgumentException("name can not be empty");
        }

        if (domain.getName().length() > 100) {
            throw new IllegalArgumentException("The number of characters in the name must be less than 100");
        }
    }
}
