package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimIdItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim claim) {
        if(claim.getClaimId() == null) {
            throw new IllegalArgumentException("The claim id cannot be null");
        }

        if(!claim.getClaimId().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException("Invalid claim id: "+ claim.getClaimId());
        }
    }

}
