package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.response.FindClaimResponseDTO;
import com.picpay.banking.jdpi.exception.NotFoundJdClientException;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.FindClaimPort;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@AllArgsConstructor
public class FindClaimPortImpl implements FindClaimPort {

    private ClaimJDClient claimJDClient;

    @Trace
    @Override
    public Claim findClaim(final String claimId, final String ispb, final boolean reivindicador) {
        return Optional.ofNullable(claimJDClient.find(claimId, ispb, reivindicador))
                .map(FindClaimResponseDTO::toClaim)
                .orElseThrow(() -> new NotFoundJdClientException("Claim not found", HttpStatus.NOT_FOUND));
    }

}
