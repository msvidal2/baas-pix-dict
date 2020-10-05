package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.request.ClaimConfirmationRequestDTO;
import com.picpay.banking.jdpi.fallbacks.JDClientExceptionFactory;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.ports.claim.ClaimConfirmationPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClaimConfirmationPortImpl implements ClaimConfirmationPort {

    private final static String CIRCUIT_BREAKER_NAME = "confirm-claim";

    private ClaimJDClient claimJDClient;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "confirmFallback")
    public Claim confirm(Claim claim, ClaimConfirmationReason reason, String requestIdentifier) {
        var requestDto = ClaimConfirmationRequestDTO.builder()
                .claimId(claim.getClaimId())
                .ispb(claim.getIspb())
                .reason(reason.getValue())
                .build();

        var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> claimJDClient.confirmation(requestIdentifier, claim.getClaimId(), requestDto),
                requestIdentifier);

        return response.toClaim();
    }

    public Claim confirmFallback(Claim claim, ClaimConfirmationReason reason, String requestIdentifier, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

}
