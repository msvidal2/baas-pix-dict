package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindByIdPort;
import com.picpay.banking.pix.core.validators.claim.ClaimCancelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class ClaimCancelUseCase {

    private final CancelClaimBacenPort cancelClaimBacenPort;

    private final FindByIdPort findByIdPort;

    private final CancelClaimPort cancelClaimPort;

    private static final Map<ClaimType, List<ClaimSituation>> situationsAllowedByType = Map.of(
        ClaimType.POSSESSION_CLAIM, List.of(ClaimSituation.AWAITING_CLAIM, ClaimSituation.CONFIRMED),
        ClaimType.PORTABILITY, List.of(ClaimSituation.AWAITING_CLAIM)
    );

    private static final Map<ClaimCancelReason, List<ClaimantType>> ownershipAllowedReasons = Map.of(
        ClaimCancelReason.CLIENT_REQUEST, List.of(ClaimantType.CLAIMANT),
        ClaimCancelReason.ACCOUNT_CLOSURE, List.of(ClaimantType.CLAIMANT),
        ClaimCancelReason.FRAUD, List.of(ClaimantType.DONOR, ClaimantType.CLAIMANT),
        ClaimCancelReason.DEFAULT_RESPONSE, List.of(ClaimantType.CLAIMANT)
    );

    private static final Map<ClaimCancelReason, List<ClaimantType>> portabilityAllowedReasons = Map.of(
        ClaimCancelReason.CLIENT_REQUEST, List.of(ClaimantType.DONOR, ClaimantType.CLAIMANT),
        ClaimCancelReason.ACCOUNT_CLOSURE, List.of(ClaimantType.CLAIMANT),
        ClaimCancelReason.FRAUD, List.of(ClaimantType.DONOR, ClaimantType.CLAIMANT),
        ClaimCancelReason.DEFAULT_RESPONSE, List.of(ClaimantType.DONOR)
    );

    public Claim execute(final Claim claimCancel,
                         final boolean canceledClaimant,
                         final ClaimCancelReason reason,
                         final String requestIdentifier) {

        ClaimCancelValidator.validate(claimCancel, canceledClaimant, reason, requestIdentifier);

        var claim = findByIdPort.find(claimCancel.getClaimId())
                .orElseThrow(ResourceNotFoundException::new);

        validateAllowedSituation(claim, canceledClaimant);
        validateAllowedReason(canceledClaimant, reason, claim);
        // TODO: validar regra quando o motivo dor DEFAULT_RESPONSE (DEFAULT_OPERATION), conforme informa no swagger

        var claimCanceled = cancelClaimBacenPort.cancel(claim.getClaimId(), reason, claimCancel.getIspb(), requestIdentifier);

        cancelClaimPort.cancel(claimCanceled, reason, requestIdentifier);

        if (claimCanceled != null)
            log.info("Claim_canceled"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("claimId", claimCanceled.getClaimId()));

        return claimCanceled;
    }

    private void validateAllowedSituation(final Claim claim, final boolean canceledClaimant) {
        var situationsAllowed = situationsAllowedByType.get(claim.getClaimType());

        if (!situationsAllowed.contains(claim.getClaimSituation())) {
            if(canceledClaimant) {
                throw new ClaimException(ClaimError.CLAIMANT_CANCEL_SITUATION_NOT_ALLOWED);
            }

            if(ClaimType.PORTABILITY.equals(claim.getClaimType())) {
                throw new ClaimException(ClaimError.PORTABILITY_CLAIM_SITUATION_NOT_ALLOW_CANCELLATION);
            }

            throw new ClaimException(ClaimError.POSSESSION_CLAIM_SITUATION_NOT_ALLOW_CANCELLATION);
        }
    }

    private void validateAllowedReason(final boolean canceledClaimant, final ClaimCancelReason reason, final Claim claim) {
        var allowedReasons = Map.of(
                ClaimType.POSSESSION_CLAIM, ownershipAllowedReasons,
                ClaimType.PORTABILITY, portabilityAllowedReasons)
                .get(claim.getClaimType())
                .get(reason);

        if (!allowedReasons.contains(ClaimantType.resolve(canceledClaimant))) {
            if(canceledClaimant) {
                throw new ClaimException(ClaimError.CLAIMANT_CANCEL_INVALID_REASON);
            }

            throw new ClaimException(ClaimError.DONOR_CANCEL_INVALID_REASON);
        }
    }

}
