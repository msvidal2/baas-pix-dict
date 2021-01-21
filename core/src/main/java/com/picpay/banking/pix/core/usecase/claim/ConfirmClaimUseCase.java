package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
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
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.claim.ConfirmClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static com.picpay.banking.pix.core.domain.ClaimConfirmationReason.ownershipConfirmReasons;
import static com.picpay.banking.pix.core.domain.ClaimConfirmationReason.portabilityConfirmReasons;
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
    private final PixKeyEventPort pixKeyEventPort;
    private final FindPixKeyPort findPixKeyPort;

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
        if (ClaimConfirmationReason.CLIENT_REQUEST == reason)
            claimConfirmed.setCompletionThresholdDate(LocalDateTime.now(ZoneId.of("UTC")));

        log.info("Claim_confirmed",
            kv(REQUEST_IDENTIFIER, requestIdentifier),
            kv(CLAIM_ID, claimConfirmed.getClaimId()));

        saveClaimPort.saveClaim(claimConfirmed, requestIdentifier);

        log.info("Claim_confirmed_saved",
            kv(REQUEST_IDENTIFIER, requestIdentifier),
            kv(CLAIM_ID, claimConfirmed.getClaimId()));

        remove(claimConfirmed, requestIdentifier).ifPresent(pixKey -> sendEvent(pixKey, requestIdentifier));

        log.info("Claim_confirmed_key_removed",
            kv(REQUEST_IDENTIFIER, requestIdentifier),
            kv(CLAIM_ID, claimConfirmed.getClaimId()),
            kv(KEY, claimConfirmed.getKey()));

        return claimConfirmed;
    }

    private Optional<PixKey> remove(Claim claimConfirmed, String requestIdentifier) {
        var pixKey = findPixKeyPort.findPixKey(claimConfirmed.getKey());
        try {
            removePixKeyPort.remove(claimConfirmed.getKey(), claimConfirmed.getIspb());
        } catch (Exception e) {
            log.error("Claim_confirmed_key_removedError",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(KEY, claimConfirmed.getKey()),
                kv(EXCEPTION, e));
        }
        return pixKey;
    }

    private void sendEvent(PixKey pixKeyRemoved, final String requestIdentifier) {
        try {
            pixKeyEventPort.pixKeyWasRemoved(pixKeyRemoved);
        } catch (Exception e) {
            log.error("Claim_confirmed_key_eventError",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(KEY, pixKeyRemoved.getKey()),
                kv(EXCEPTION, e));
        }
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
                && ClaimConfirmationReason.DEFAULT_RESPONSE == claim.getConfirmationReason()
                && LocalDateTime.now(ZoneId.of("UTC")).isBefore(claim.getResolutionThresholdDate())) {
                    throw new ClaimException(ClaimError.OWNERSHIP_DEFAULT_OPERATION_RESOLUTION_DATE_NOT_PASSED);
        }
    }

}
