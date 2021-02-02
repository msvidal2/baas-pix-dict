package com.picpay.banking.pix.core.validators.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class ListClaimValidator {

    public static void validate(final String requestIdentifier, final Claim claim, final Integer limit) {
        if (Strings.isNullOrEmpty(requestIdentifier) || requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier cannot be empty");
        }

        if(Objects.isNull(claim)) {
            throw new IllegalArgumentException("Claim cannot be null");
        }

        if(limit <= 0){
            throw new IllegalArgumentException("Limit greater than 0 (zero) is required.");
        }

        ClaimIspbItemValidator.validate(claim.getIspb());
    }

}
