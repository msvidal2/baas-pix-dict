package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.CreateClaimConverter;
import com.picpay.banking.jdpi.dto.request.CreateClaimRequestDTO;
import com.picpay.banking.jdpi.dto.response.ClaimResponseDTO;
import com.picpay.banking.jdpi.fallbacks.ClaimJDClientFallback;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.CreateClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CreateClaimPortImpl implements CreateClaimPort {

    private final static String CIRCUIT_BREAKER_NAME = "create-claim";

    private ClaimJDClient claimJDClient;

    private CreateClaimConverter converter;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "createClaimFallback")
    public Claim createClaim(final Claim claim, final String requestIdentifier) {
        CreateClaimRequestDTO requestDTO = converter.convert(claim);

        ClaimResponseDTO responseDTO = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> claimJDClient.createClaim(requestIdentifier, requestDTO), requestIdentifier);

        return converter.convert(claim, responseDTO);
    }

    public Claim createClaimFallback(final Claim claim, final String requestIdentifier, Exception e) {
        new ClaimJDClientFallback(e).createClaim(null, null);
        return null;
    }

}
