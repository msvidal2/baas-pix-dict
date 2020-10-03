package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.response.FindClaimResponseDTO;
import com.picpay.banking.jdpi.exception.NotFoundJdClientException;
import com.picpay.banking.jdpi.fallbacks.ClaimJDClientFallback;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.FindClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@AllArgsConstructor
public class FindClaimPortImpl implements FindClaimPort {

    private final static String CIRCUIT_BREAKER_NAME = "find-claim";

    private ClaimJDClient claimJDClient;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findClaimFallback")
    public Claim findClaim(final String claimId, final String ispb, final boolean reivindicador) {

        final var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> claimJDClient.find(claimId, ispb, reivindicador));

        return Optional.ofNullable(response)
                .map(FindClaimResponseDTO::toClaim)
                .orElseThrow(() -> new NotFoundJdClientException("Claim not found", HttpStatus.NOT_FOUND));
    }

    public Claim findClaimFallback(final String claimId, final String ispb, final boolean reivindicador, Exception e) {
        new ClaimJDClientFallback(e).find(null, null, false);
        return null;
    }

}
