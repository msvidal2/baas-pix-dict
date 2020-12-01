package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimAccountTypeItemValidator {

    public static void validate(AccountType accountType) {
        if (accountType == null) {
            throw new IllegalArgumentException("accountType can not be empty");
        }
    }
}
