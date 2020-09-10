package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimIdtemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim claim) {
        if (claim.getClaimId() == null) {
            throw new IllegalArgumentException("ispb can not be empty");
        }
        if(!claim.getClaimId().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            throw new UseCaseException("Invalid claim id: "+ claim.getClaimId());
        }
    }
}
