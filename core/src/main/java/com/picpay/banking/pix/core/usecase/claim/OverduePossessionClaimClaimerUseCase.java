package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class OverduePossessionClaimClaimerUseCase {

    private final CancelClaimPort cancelClaimPort;
    private final CancelClaimBacenPort cancelClaimBacenPort;

    public void cancel(Claim claim, String requestIdentifier) {

        cancelClaimBacenPort.cancel(claim.getClaimId(), ClaimCancelReason.DEFAULT_RESPONSE, claim.getIspb(), requestIdentifier);

        claim.setClaimSituation(ClaimSituation.CANCELED);

        cancelClaimPort.cancel(claim, ClaimCancelReason.DEFAULT_RESPONSE, requestIdentifier);

        log.info("OverduePossessionClaim_canceled",
                kv("claimId", claim.getClaimId()));
    }

}
