package com.picpay.banking.pix.core.validators.addressingkey;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class AccountNumberItemValidator implements DictItemValidator<AddressingKey> {

    @Override
    public void validate(AddressingKey addressingKey) {
        if (Strings.isNullOrEmpty(addressingKey.getAccountNumber())) {
            throw new IllegalArgumentException("accountNumber can not be empty");
        }
        if (addressingKey.getAccountNumber().length() < 4 || addressingKey.getAccountNumber().length() > 20) {
            throw new IllegalArgumentException("The number of characters in the accountNumber must be between 4 and 20");
        }
    }
}
