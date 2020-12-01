package com.picpay.banking.pix.core.validators.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimAccountNumberItemValidator {

    public static void validate(String accountNumber) {
        if (Strings.isNullOrEmpty(accountNumber)) {
            throw new IllegalArgumentException("accountNumber can not be empty");
        }
        if (accountNumber.length() < 4 || accountNumber.length() > 20) {
            throw new IllegalArgumentException("The number of characters in the accountNumber must be between 4 and 20");
        }
    }
}
