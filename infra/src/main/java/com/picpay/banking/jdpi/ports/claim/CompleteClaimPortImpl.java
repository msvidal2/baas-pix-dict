package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.request.CompleteClaimRequestDTO;
import com.picpay.banking.jdpi.fallbacks.JDClientExceptionFactory;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CompleteClaimPortImpl implements CompleteClaimPort {

    private final static String CIRCUIT_BREAKER_NAME = "complete-claim";

    private ClaimJDClient claimJDClient;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "completeFallback")
    public Claim complete(Claim claim, String requestIdentifier) {
        final var request = CompleteClaimRequestDTO.builder()
                .claimId(claim.getClaimId())
                .ispb(claim.getIspb())
                .build();

        var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> claimJDClient.complete(requestIdentifier, claim.getClaimId(), request),
                requestIdentifier);

        return response.toClaim();
    }

    public Claim completeFallback(Claim claim, String requestIdentifier, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

}