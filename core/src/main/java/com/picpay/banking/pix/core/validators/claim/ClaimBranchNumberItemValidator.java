package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.BranchNumberItemValidator;

public class ClaimBranchNumberItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim domain) {
        new BranchNumberItemValidator()
            .validate(AddressingKey.builder().branchNumber(domain.getBranchNumber()).build());
    }
}
