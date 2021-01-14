package com.picpay.banking.pix.core.validators.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.validators.ClaimIdValidator;
import lombok.AllArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CompleteClaimValidator {

    public static void validate(final String requestIdentifier, final Claim claim) {
        if (StringUtils.isBlank(requestIdentifier)) {
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
                throw new ClaimException(ClaimError.INVALID_CLAIM_COMPLETED_SITUATION);

            if(ClaimType.POSSESSION_CLAIM.equals(claimResult.getClaimType()) &&
                    claimResult.getCompletionThresholdDate().isAfter(LocalDateTime.now(ZoneId.of("UTC"))))
                throw new ClaimException(ClaimError.INVALID_CLAIM_COMPLETED_SITUATION);
        });
    }
}
