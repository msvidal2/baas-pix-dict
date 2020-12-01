package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.BranchNumberValidator;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimBranchNumberItemValidator {

    public static void validate(String branchNumber) {
        BranchNumberValidator.validate(branchNumber);
    }

}
