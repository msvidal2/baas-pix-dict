package com.picpay.banking.pix.core.validators.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimNameItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim domain) {
        if (Strings.isNullOrEmpty(domain.getName())) {
            throw new IllegalArgumentException("name can not be empty");
        }

        if (domain.getName().length() > 100) {
            throw new IllegalArgumentException("The number of characters in the name must be less than 100");
        }
    }
}
