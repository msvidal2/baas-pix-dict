package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class FantasyNameItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey domain) {
        if (domain.getFantasyName() == null) {
            return;
        }

        if (domain.getFantasyName().length() > 100) {
            throw new IllegalArgumentException("The number of characters in the fantasyName must be less than 100");
        }
    }
}
