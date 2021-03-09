package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.Reason;
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
        claim.setClaimSituation(ClaimSituation.CANCELED);
        claim.setReason(Reason.DEFAULT_RESPONSE);

        cancelClaimBacenPort.cancel(claim.getClaimId(), Reason.DEFAULT_RESPONSE, claim.getIspb(), requestIdentifier);
        cancelClaimPort.cancel(claim, requestIdentifier);

        log.info("OverduePossessionClaim_canceled",
                kv("claimId", claim.getClaimId()));
    }

}
