package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.FindClaimPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class FindClaimUseCase {

    private FindClaimPort findClaimPort;

    public Claim execute(final String claimId, final String ispb, final boolean reivindicador)  {
        Claim claimFound = findClaimPort.findClaim(claimId, ispb, reivindicador);

        if (claimFound != null)
            log.info("Claim_found", kv("claimId", claimFound.getClaimId()));

        return claimFound;
    }

}