package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToCancelPortabilityPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static com.picpay.banking.pix.core.domain.ExecutionType.CANCEL_PORTABILITY_POLLING;

@Slf4j
@RequiredArgsConstructor
public class OverduePortabilityClaimUseCase {

    private final FindClaimToCancelPort findClaimToCancelPort;
    private final CancelClaimBacenPort cancelClaimBacenPort;
    private final CancelClaimPort cancelClaimPort;
    private final ExecutionPort executionPort;
    private final SendToCancelPortabilityPort sendToCancelPortabilityPort;

    public void execute(String ispb, Integer limit) {
        try {
            poll(ispb, limit);
            executionPort.lastExecution(CANCEL_PORTABILITY_POLLING);
        } catch (Exception e) {
            log.error("Cancel Portability Polling failed: ", e);
            executionPort.lastExecution(CANCEL_PORTABILITY_POLLING);
        }
    }

    private void poll(final String ispb, final Integer limit) {
        log.info("Polling database for portabilities to cancel");
        List<Claim> claimsToCancel = findClaimToCancelPort.findClaimToCancelWhereIsDonor(ClaimType.PORTABILITY, List.of(ClaimSituation.AWAITING_CLAIM), Integer.parseInt(ispb),
                LocalDateTime.now(ZoneId.of("UTC")), limit);

        log.debug("Portabilities to cancel found: {}", claimsToCancel.size());
        claimsToCancel.forEach(sendToCancelPortabilityPort::send);
    }

    public void cancelClaim(Claim claim, final String donorParticipant) {
        String requestIdentifier = UUID.randomUUID().toString();
        claim.setClaimSituation(ClaimSituation.CANCELED);
        claim.setCancelReason(ClaimReason.DEFAULT_OPERATION);

        cancelClaimBacenPort.cancel(claim.getClaimId(), ClaimReason.DEFAULT_OPERATION, Integer.parseInt("22896431"), requestIdentifier);
        cancelClaimPort.cancel(claim, requestIdentifier);

        log.debug("Portability canceled : " + claim.getClaimId());
    }

}