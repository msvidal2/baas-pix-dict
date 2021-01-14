package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindByIdPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.validators.claim.ClaimCancelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class CancelClaimUseCase {

    private final CancelClaimBacenPort cancelClaimBacenPort;
    private final FindByIdPort findByIdPort;
    private final CancelClaimPort cancelClaimPort;
    private final FindPixKeyPort findPixKeyPort;
    private final SavePixKeyPort savePixKeyPort;

    public Claim execute(final Claim claimCancel,
                         final boolean canceledClaimant,
                         final ClaimCancelReason reason,
                         final String requestIdentifier) {

        ClaimCancelValidator.validate(claimCancel, canceledClaimant, reason, requestIdentifier);

        var claim = findByIdPort.find(claimCancel.getClaimId())
                .orElseThrow(ResourceNotFoundException::new);

        validateAllowedSituation(claim, canceledClaimant);
        validateAllowedReason(claim, reason, canceledClaimant);
        validateExpiredResolutionPeriod(claim, reason, canceledClaimant);

        var claimCanceled = cancelClaimBacenPort.cancel(claim.getClaimId(), reason, claimCancel.getIspb(), requestIdentifier);

        cancelClaimPort.cancel(claimCanceled, reason, requestIdentifier);

        if (isPossessionClaimDonationFraud(claim, reason)) {
            recoveryDonatedByFraudKey(claim);
        }

        if (claimCanceled != null)
            log.info("Claim_canceled",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimCanceled.getClaimId()));

        return claimCanceled;
    }

    private void validateAllowedSituation(final Claim claim, final boolean canceledClaimant) {
        var situationsAllowed = ClaimSituation
                .getCancelSituationsAllowedByType()
                .get(claim.getClaimType());

        if (!situationsAllowed.contains(claim.getClaimSituation())) {
            if (canceledClaimant) {
                throw new ClaimException(ClaimError.CLAIMANT_CANCEL_SITUATION_NOT_ALLOWED);
            }

            if (ClaimType.PORTABILITY.equals(claim.getClaimType())) {
                throw new ClaimException(ClaimError.PORTABILITY_CLAIM_SITUATION_NOT_ALLOW_CANCELLATION);
            }

            throw new ClaimException(ClaimError.POSSESSION_CLAIM_SITUATION_NOT_ALLOW_CANCELLATION);
        }
    }

    private void validateAllowedReason(final Claim claim,
                                       final ClaimCancelReason reason,
                                       final boolean canceledClaimant) {
        var allowedReasons = Map.of(
                ClaimType.POSSESSION_CLAIM, ClaimCancelReason.getOwnershipAllowedReasons(),
                ClaimType.PORTABILITY, ClaimCancelReason.getPortabilityAllowedReasons())
                    .get(claim.getClaimType())
                    .get(reason);

        if (!allowedReasons.contains(ClaimantType.resolve(canceledClaimant))) {
            if (canceledClaimant) {
                throw new ClaimException(ClaimError.CLAIMANT_CANCEL_INVALID_REASON);
            }
            throw new ClaimException(ClaimError.DONOR_CANCEL_INVALID_REASON);
        }
    }

    private void validateExpiredResolutionPeriod(final Claim claim,
                                                 final ClaimCancelReason reason,
                                                 final boolean canceledClaimant) {
        if (ClaimCancelReason.DEFAULT_RESPONSE.equals(reason)
                && LocalDateTime.now().isBefore(claim.getResolutionThresholdDate())) {
            if (canceledClaimant) {
                throw new ClaimException(ClaimError.CLAIMANT_CANCEL_INVALID_REASON);
            }
            throw new ClaimException(ClaimError.DONOR_CANCEL_INVALID_REASON);
        }
    }

    private boolean isPossessionClaimDonationFraud(Claim claim, ClaimCancelReason reason) {
        return ClaimType.POSSESSION_CLAIM.equals(claim.getClaimType())
                && ClaimSituation.CONFIRMED.equals(claim.getClaimSituation())
                && ClaimConfirmationReason.DEFAULT_RESPONSE.equals(claim.getConfirmationReason())
                && ClaimCancelReason.FRAUD.equals(reason);
    }

    private void recoveryDonatedByFraudKey(Claim claim) {
        PixKey pixkey = findPixKeyPort
                .findDonatedPixKey(claim.getKey())
                .orElseThrow(ResourceNotFoundException::new);

        pixkey.setDonatedAutomatically(false);
        savePixKeyPort.savePixKey(pixkey, Reason.FRAUD);
    }

}
