package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class KeyTypeItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey domain) {
        if (domain.getType() == null) {
            throw new IllegalArgumentException("type can not be empty");
        }
    }
}
