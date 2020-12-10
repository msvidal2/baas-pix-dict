package com.picpay.banking.ports;

import com.picpay.banking.clients.BacenClaimClient;
import com.picpay.banking.dto.request.CompleteClaimRequest;
import com.picpay.banking.config.TimeLimiterExecutor;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompleteClaimPortBacenImpl implements CompleteClaimBacenPort {

    private static final String CIRCUIT_BREAKER_NAME = "complete-claim-bacen";

    private final BacenClaimClient bacenClaimClient;

    private final TimeLimiterExecutor timeLimiterExecutor;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "completeClaimFallback")
    public Claim complete(Claim claim, String requestIdentifier) {
        var request = CompleteClaimRequest.from(claim, requestIdentifier);

        var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> bacenClaimClient.completeClaim(claim.getClaimId(), request),
                requestIdentifier);

        return response.toClaim();
    }

    public Claim completeClaimFallback(Claim claim, String requestIdentifier, Exception e) {
        log.error("Claim_fallback_completingBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("claimType", claim.getClaimType()),
                kv("key", claim.getKey()),
                kv("cpfCnpf", claim.getCpfCnpj()),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e).build();
    }
}
