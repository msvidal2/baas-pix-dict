package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class IspbClaimItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim domain) {
        if (domain.getIspb() < 0 || String.valueOf(domain.getIspb()).length() > 8) {
            throw new IllegalArgumentException("The number of characters in the idpb must be less than 8");
        }
    }
}
