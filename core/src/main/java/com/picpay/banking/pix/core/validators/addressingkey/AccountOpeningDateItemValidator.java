package com.picpay.banking.pix.core.validators.addressingkey;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class AccountOpeningDateItemValidator implements DictItemValidator<AddressingKey> {

    @Override
    public void validate(AddressingKey domain) {
        if (domain.getAccountOpeningDate() == null) {
            throw new IllegalArgumentException("accountOpeningDate can not be empty");
        }
    }
}
