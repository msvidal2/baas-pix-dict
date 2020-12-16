package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.picpay.banking.pix.core.domain.ClaimConfirmationReason.DEFAULT_RESPONSE;
import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class OverduePossessionClaimUseCase {

    private final ConfirmClaimPort confirmClaimPort;

    private final CreateClaimPort saveClaimPort;

    public void execute(Claim claim, String requestIdentifier) {

        claim = confirmClaimPort.confirm(claim, DEFAULT_RESPONSE, requestIdentifier);

        claim.setConfirmationReason(DEFAULT_RESPONSE);

        saveClaimPort.saveClaim(claim, requestIdentifier);

        log.info("OverduePossessionClaim_confirmed", kv("claimId", claim.getClaimId()));

        saveClaimPort.saveClaim(claim, requestIdentifier);

        log.info("OverduePossessionClaim_confirmed_saved", kv("claimId", claim.getClaimId()));

        //TODO: delete logico
    }

}
