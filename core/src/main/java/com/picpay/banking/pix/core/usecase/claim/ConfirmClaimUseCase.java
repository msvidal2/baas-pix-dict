package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.claim.ConfirmClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static com.picpay.banking.pix.core.domain.ClaimReason.ownershipConfirmReasons;
import static com.picpay.banking.pix.core.domain.ClaimReason.portabilityConfirmReasons;
import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class ConfirmClaimUseCase {

    public static final String REQUEST_IDENTIFIER = "requestIdentifier";
    public static final String CLAIM_ID = "claimId";
    public static final String KEY = "key";
    public static final String EXCEPTION = "exception";

    private final ConfirmClaimPort confirmClaimPort;
    private final FindClaimPort findClaimPort;
    private final CreateClaimPort saveClaimPort;
    private final RemovePixKeyPort removePixKeyPort;
    private final FindPixKeyPort findPixKeyPort;

    public Claim execute(final Claim confirmClaim,
        final ClaimReason reason,
        final String requestIdentifier) {

        ConfirmClaimValidator.validate(confirmClaim, reason, requestIdentifier);

        Claim claim = findClaimPort.findClaim(confirmClaim.getClaimId(), confirmClaim.getIspb(), false)
            .orElseThrow(ResourceNotFoundException::new);

        claim.setConfirmationReason(reason);

        validateClaimSituation(claim);
        validateClaimReason(claim);
        validateResolutionPeriod(claim);

        Claim claimConfirmed = confirmClaimPort.confirm(claim, reason, requestIdentifier);
        if (ClaimReason.CLIENT_REQUEST == reason)
            claimConfirmed.setCompletionThresholdDate(LocalDateTime.now(ZoneId.of("UTC")));

        log.info("Claim_confirmed",
            kv(REQUEST_IDENTIFIER, requestIdentifier),
            kv(CLAIM_ID, claimConfirmed.getClaimId()));

        try {
            saveClaimPort.saveClaim(claimConfirmed, requestIdentifier);
        } catch (Exception e){
            log.error("Error saving in the database: {} ", e);
        }

        log.info("Claim_confirmed_saved",
            kv(REQUEST_IDENTIFIER, requestIdentifier),
            kv(CLAIM_ID, claimConfirmed.getClaimId()));

        remove(claimConfirmed, requestIdentifier);

        log.info("Claim_confirmed_key_removed",
            kv(REQUEST_IDENTIFIER, requestIdentifier),
            kv(CLAIM_ID, claimConfirmed.getClaimId()),
            kv(KEY, claimConfirmed.getPixKey().getKey()));

        return claimConfirmed;
    }

    private Optional<PixKey> remove(Claim claimConfirmed, String requestIdentifier) {
        var pixKey = findPixKeyPort.findPixKey(claimConfirmed.getPixKey().getKey());
        try {
            removePixKeyPort.remove(claimConfirmed.getPixKey().getKey(), claimConfirmed.getIspb());
        } catch (Exception e) {
            log.error("Claim_confirmed_key_removedError",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(KEY, claimConfirmed.getPixKey().getKey()),
                kv(EXCEPTION, e));
        }
        return pixKey;
    }

    private void validateClaimSituation(Claim claim) {
        if (ClaimSituation.AWAITING_CLAIM != claim.getClaimSituation()) {
            throw new ClaimException(ClaimError.INVALID_CLAIM_SITUATION);
        }
    }

    private void validateClaimReason(Claim claim) {
        if (ClaimType.PORTABILITY == claim.getClaimType()
                && !portabilityConfirmReasons().contains(claim.getConfirmationReason())) {
            throw new ClaimException(ClaimError.INVALID_CLAIM_REASON);
        }
        if (ClaimType.POSSESSION_CLAIM == claim.getClaimType()
                && !ownershipConfirmReasons().contains(claim.getConfirmationReason())) {
            throw new ClaimException(ClaimError.INVALID_CLAIM_REASON);
        }
    }

    private void validateResolutionPeriod(Claim claim) {
        if (ClaimType.POSSESSION_CLAIM == claim.getClaimType()
                && ClaimReason.DEFAULT_OPERATION == claim.getConfirmationReason()
                && LocalDateTime.now(ZoneId.of("UTC")).isBefore(claim.getResolutionThresholdDate())) {
                    throw new ClaimException(ClaimError.OWNERSHIP_DEFAULT_OPERATION_RESOLUTION_DATE_NOT_PASSED);
        }
    }

}
