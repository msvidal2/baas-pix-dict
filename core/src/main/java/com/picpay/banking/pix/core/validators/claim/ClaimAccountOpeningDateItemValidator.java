package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimAccountOpeningDateItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim domain) {
        if (domain.getAccountOpeningDate() == null) {
            throw new IllegalArgumentException("accountOpeningDate can not be empty");
        }
    }
}
