package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.request.CreateClaimRequest;
import com.picpay.banking.config.TimeLimiterExecutor;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateClaimPortBacenImpl implements CreateClaimBacenPort {

    private static final String CIRCUIT_BREAKER_NAME = "create-claim-bacen";

    private final BacenClaimClient bacenClaimClient;

    private final TimeLimiterExecutor timeLimiterExecutor;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "createClaimFallback")
    public Claim createClaim(Claim claim, String requestIdentifier) {

        var request = CreateClaimRequest.from(claim);

        var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> bacenClaimClient.createClaim(request),
                requestIdentifier);

        return response.toClaim();
    }

    public Claim createClaimFallback(Claim claim, String requestIdentifier, Exception e) {
        log.error("Claim_fallback_creatingBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("claimType", claim.getClaimType()),
                kv("key", claim.getKey()),
                kv("cpfCnpf", claim.getCpfCnpj()),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e).build();
    }

}
