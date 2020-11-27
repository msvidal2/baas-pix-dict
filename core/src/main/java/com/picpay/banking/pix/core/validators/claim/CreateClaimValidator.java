package com.picpay.banking.pix.core.validators.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class CreateClaimValidator {

    public static void validate(final String requestIdentifier, final Claim claim) {
        if (Strings.isNullOrEmpty(requestIdentifier) || requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier cannot be empty");
        }

        if(Objects.isNull(claim)) {
            throw new IllegalArgumentException("Claim cannot be null");
        }

        ClaimAccountNumberItemValidator.validate(claim.getAccountNumber());
        ClaimAccountTypeItemValidator.validate(claim.getAccountType());
        ClaimBranchNumberItemValidator.validate(claim.getBranchNumber());
        ClaimCpfCnpjItemValidator.validate(claim.getTaxId());
        ClaimIspbItemValidator.validate(claim.getIspb());
        ClaimNameItemValidator.validate(claim.getName());
        ClaimFantasyNameItemValidator.validate(claim.getFantasyName());
    }

}
