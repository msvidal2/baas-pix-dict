package com.picpay.banking.pix.core.validators.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimAccountNumberItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim claim) {
        if (Strings.isNullOrEmpty(claim.getAccountNumber())) {
            throw new IllegalArgumentException("accountNumber can not be empty");
        }
        if (claim.getAccountNumber().length() < 4 || claim.getAccountNumber().length() > 20) {
            throw new IllegalArgumentException("The number of characters in the accountNumber must be between 4 and 20");
        }
    }
}
