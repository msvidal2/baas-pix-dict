package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class FindClaimUseCase {

    private FindClaimPort findClaimPort;

    public Claim execute(final String claimId, final String ispb, final boolean reivindicador)  {

        Optional<Claim> optClaim = findClaimPort.findClaim(claimId, ispb, reivindicador);

        optClaim.ifPresent(claim -> log.info("Claim_found", kv("claimId", claim.getClaimId())));

        return optClaim.orElse(null);
    }

}