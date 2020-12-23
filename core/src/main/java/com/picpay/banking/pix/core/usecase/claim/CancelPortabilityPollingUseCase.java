package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CancelPortabilityPollingUseCase {

    private final FindClaimToCancelPort findClaimToCancelPort;
    private final CancelClaimBacenPort cancelClaimBacenPort;
    private final CancelClaimPort cancelClaimPort;

    public void execute(String ispb, Integer limit) {
        try {
            poll(ispb, limit);
        } catch (Exception e) {
            log.error("Cancel Portability Polling failed: ", e);
        }
    }

    private void poll(final String ispb, final Integer limit) {
        log.info("Polling database for portabilities to cancel");
        List<Claim> claimsToCancel = findClaimToCancelPort.find(ClaimType.PORTABILITY, ClaimSituation.getPending(), Integer.parseInt(ispb),
                LocalDateTime.now(), limit);

        log.debug("Portabilities to cancel found: {}", claimsToCancel.size());
        claimsToCancel.forEach(c -> cancelClaim(c, ispb));
        log.debug("Portabilities canceled");
    }

    private void cancelClaim(Claim claim, String donorParticipant){
        String requestIdentifier = UUID.randomUUID().toString();
        claim.setClaimSituation(ClaimSituation.CANCELED);
        cancelClaimBacenPort.cancel(claim.getClaimId(), ClaimCancelReason.DEFAULT_RESPONSE, Integer.parseInt(donorParticipant), requestIdentifier);
        cancelClaimPort.cancel(claim, ClaimCancelReason.DEFAULT_RESPONSE, requestIdentifier);
    }

}