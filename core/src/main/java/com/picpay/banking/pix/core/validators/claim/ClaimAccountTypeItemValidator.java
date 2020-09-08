package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimAccountTypeItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim domain) {
        if (domain.getAccountType() == null) {
            throw new IllegalArgumentException("accountType can not be empty");
        }
    }
}
