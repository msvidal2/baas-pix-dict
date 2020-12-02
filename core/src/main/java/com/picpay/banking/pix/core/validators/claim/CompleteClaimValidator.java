package com.picpay.banking.pix.core.validators.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.validators.ClaimIdValidator;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CompleteClaimValidator {

    public static void validate(final String requestIdentifier, final Claim claim) {
        if (Strings.isNullOrEmpty(requestIdentifier) || requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("You must inform a request identifier");
        }

        if(Objects.isNull(claim)) {
            throw new IllegalArgumentException("Claim cannot be null");
        }

        ClaimIdValidator.validate(claim.getClaimId());
        ClaimIspbItemValidator.validate(claim.getIspb());
    }

    public static void validateClaimSituation(Optional<Claim> optClaim){
        optClaim.ifPresent(claimResult -> {
            if(!ClaimSituation.CONFIRMED.equals(claimResult.getClaimSituation()))
                throw new ClaimException(ClaimError.JDPIRVN011);

            if(ClaimType.POSSESSION_CLAIM.equals(claimResult.getClaimType()) &&
                    claimResult.getCompletionThresholdDate().isAfter(LocalDateTime.now()))
                throw new ClaimException(ClaimError.JDPIRVN011);
        });
    }
}
