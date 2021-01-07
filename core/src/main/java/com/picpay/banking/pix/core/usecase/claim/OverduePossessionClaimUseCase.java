package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyAutomaticallyPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.picpay.banking.pix.core.domain.ClaimConfirmationReason.DEFAULT_RESPONSE;
import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class OverduePossessionClaimUseCase {

    private final ConfirmClaimPort confirmClaimPort;

    private final CreateClaimPort saveClaimPort;

    private final RemovePixKeyAutomaticallyPort removePixKeyAutomaticallyPort;

    private final CancelClaimPort cancelClaimPort;
    private final CancelClaimBacenPort cancelClaimBacenPort;

    public void confirm(Claim claim, String requestIdentifier) {

        claim = confirmClaimPort.confirm(claim, DEFAULT_RESPONSE, requestIdentifier);

        claim.setConfirmationReason(DEFAULT_RESPONSE);

        log.info("OverduePossessionClaim_confirmed",
                kv("claimId", claim.getClaimId()));

        saveClaimPort.saveClaim(claim, requestIdentifier);

        log.info("OverduePossessionClaim_confirmed_saved",
                kv("claimId", claim.getClaimId()));

        removePixKeyAutomaticallyPort.remove(claim.getKey(), claim.getCompletionThresholdDate());

        log.info("OverduePossessionClaim_key_removed",
                kv("claimId", claim.getClaimId()),
                kv("key", claim.getKey()));
    }

    public void cancel(Claim claim, String requestIdentifier) {
        cancelClaimBacenPort.cancel(claim.getClaimId(), ClaimCancelReason.DEFAULT_RESPONSE, claim.getIspb(), requestIdentifier);
        claim.setClaimSituation(ClaimSituation.CANCELED);
        cancelClaimPort.cancel(claim, ClaimCancelReason.DEFAULT_RESPONSE, requestIdentifier);

        log.info("OverduePossessionClaim_canceled",
                kv("claimId", claim.getClaimId()));
    }

}
