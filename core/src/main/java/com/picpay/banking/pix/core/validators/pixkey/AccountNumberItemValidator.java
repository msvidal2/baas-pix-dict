package com.picpay.banking.pix.core.validators.pixkey;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class AccountNumberItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey pixKey) {
        if (Strings.isNullOrEmpty(pixKey.getAccountNumber())) {
            throw new IllegalArgumentException("accountNumber can not be empty");
        }
        if (pixKey.getAccountNumber().length() < 4 || pixKey.getAccountNumber().length() > 20) {
            throw new IllegalArgumentException("The number of characters in the accountNumber must be between 4 and 20");
        }
    }
}
