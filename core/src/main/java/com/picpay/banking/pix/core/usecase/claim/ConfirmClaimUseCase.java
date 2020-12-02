package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.claim.ConfirmClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.core.domain.ClaimConfirmationReason.ownershipConfirmReasons;
import static com.picpay.banking.pix.core.domain.ClaimConfirmationReason.portabilityConfirmReasons;
import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class ConfirmClaimUseCase {

    private final ConfirmClaimPort confirmClaimPort;

    private final FindClaimPort findClaimPort;

    private final CreateClaimPort saveClaimPort;

    private final RemovePixKeyPort removePixKeyPort;

    public Claim execute(final Claim confirmClaim,
                         final ClaimConfirmationReason reason,
                         final String requestIdentifier) {

        ConfirmClaimValidator.validate(confirmClaim, reason, requestIdentifier);

        Claim claim = findClaimPort.findClaim(confirmClaim.getClaimId(), confirmClaim.getIspb(), false)
                .orElseThrow(ResourceNotFoundException::new);

        claim.setConfirmationReason(reason);

        validateClaimSituation(claim);
        validateClaimReason(claim);
        validateResolutionPeriod(claim);

        Claim claimConfirmed = confirmClaimPort.confirm(claim, reason, requestIdentifier);

        log.info("Claim_confirmed",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimConfirmed.getClaimId()));

        saveClaimPort.saveClaim(claimConfirmed, requestIdentifier);

        log.info("Claim_confirmed_saved",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimConfirmed.getClaimId()));

        removePixKeyPort.remove(claimConfirmed.getKey(), claimConfirmed.getIspb());

        log.info("Claim_confirmed_key_removed",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimConfirmed.getClaimId()),
                kv("key", claimConfirmed.getKey()));

        return claimConfirmed;
    }

    private void validateClaimSituation(Claim claim) {
        if (ClaimSituation.AWAITING_CLAIM != claim.getClaimSituation()) {
            throw new ClaimException(ClaimError.INVALID_CLAIM_SITUATION);
        }
    }

    private void validateClaimReason(Claim claim) {
        if (ClaimType.PORTABILITY.equals(claim.getClaimType())
                && !portabilityConfirmReasons().contains(claim.getConfirmationReason())) {
            throw new ClaimException(ClaimError.INVALID_CLAIM_REASON);
        }
        if (ClaimType.POSSESSION_CLAIM.equals(claim.getClaimType())
                && !ownershipConfirmReasons().contains(claim.getConfirmationReason())) {
            throw new ClaimException(ClaimError.INVALID_CLAIM_REASON);
        }
    }

    private void validateResolutionPeriod(Claim claim) {
        if (ClaimType.POSSESSION_CLAIM.equals(claim.getClaimType())
                && ClaimConfirmationReason.DEFAULT_RESPONSE.equals(claim.getConfirmationReason())
                && LocalDateTime.now().isBefore(claim.getResolutionThresholdDate())) {
                    throw new ClaimException(ClaimError.OWNERSHIP_DEFAULT_OPERATION_RESOLUTION_DATE_NOT_PASSED);
        }
    }

}
