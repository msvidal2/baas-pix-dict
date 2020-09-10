package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class AccountOpeningDateItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey domain) {
        if (domain.getAccountOpeningDate() == null) {
            throw new IllegalArgumentException("accountOpeningDate can not be empty");
        }
    }
}
