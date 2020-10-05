package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.pixkey.BranchNumberItemValidator;

public class ClaimBranchNumberItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim domain) {
        new BranchNumberItemValidator()
            .validate(PixKey.builder().branchNumber(domain.getBranchNumber()).build());
    }
}
