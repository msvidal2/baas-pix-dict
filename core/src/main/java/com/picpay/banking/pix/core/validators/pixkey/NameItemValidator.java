package com.picpay.banking.pix.core.validators.pixkey;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class NameItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey domain) {
        if (Strings.isNullOrEmpty(domain.getName())) {
            throw new IllegalArgumentException("name can not be empty");
        }

        if (domain.getName().length() > 100) {
            throw new IllegalArgumentException("The number of characters in the name must be less than 100");
        }
    }
}
