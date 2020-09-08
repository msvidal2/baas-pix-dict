package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimIspbItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim claim) {
        if (claim.getIspb() <= 0) {
            throw new IllegalArgumentException("Invalid ISPB");
        }
    }
}
