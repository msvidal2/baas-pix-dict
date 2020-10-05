package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimPersonTypeItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim domain) {
        if (domain.getPersonType() == null) {
            throw new IllegalArgumentException("personType can not be empty");
        }
    }
}