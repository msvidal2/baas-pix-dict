package com.picpay.banking.pix.core.validators.addressingkey;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class AccountTypeItemValidator implements DictItemValidator<AddressingKey> {

    @Override
    public void validate(AddressingKey domain) {
        if (domain.getAccountType() == null) {
            throw new IllegalArgumentException("accountType can not be empty");
        }
    }
}
