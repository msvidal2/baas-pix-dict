package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class AccountTypeItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey domain) {
        if (domain.getAccountType() == null) {
            throw new IllegalArgumentException("accountType can not be empty");
        }
    }
}
