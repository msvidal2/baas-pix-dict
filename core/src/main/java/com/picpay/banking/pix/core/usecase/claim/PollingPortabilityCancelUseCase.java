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
public class PollingPortabilityCancelUseCase {

    private final FindClaimToCancelPort findClaimToCancelPort;

    private final CancelClaimBacenPort cancelClaimBacenPort;

    private final CancelClaimPort cancelClaimPort;

    public List<Claim> execute(Integer donorParticipant, Integer limit) {

        List<Claim> claimsToCancel = findClaimToCancelPort.find(ClaimType.PORTABILITY, ClaimSituation.getPending(), donorParticipant,
                LocalDateTime.now(), limit);

        log.debug("Claims to cancel found: {}", claimsToCancel.size());

        claimsToCancel.forEach(c -> cancelClaim(c, donorParticipant));

        log.debug("Claims canceled");

        return claimsToCancel;
    }

    private void cancelClaim(Claim claim, Integer donorParticipant){
        String requestIdentifier = UUID.randomUUID().toString();
        cancelClaimBacenPort.cancel(claim.getClaimId(), ClaimCancelReason.DEFAULT_RESPONSE, donorParticipant, requestIdentifier);
        cancelClaimPort.cancel(claim, ClaimCancelReason.DEFAULT_RESPONSE, requestIdentifier);
    }

}