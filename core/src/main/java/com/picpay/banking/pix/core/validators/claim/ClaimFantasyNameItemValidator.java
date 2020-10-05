package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimFantasyNameItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim domain) {
        if (domain.getFantasyName() == null) {
            return;
        }

        if (domain.getFantasyName().length() > 100) {
            throw new IllegalArgumentException("The number of characters in the fantasyName must be less than 100");
        }
    }
}
