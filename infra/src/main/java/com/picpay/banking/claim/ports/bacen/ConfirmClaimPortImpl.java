package com.picpay.banking.claim.ports.bacen;

import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.request.ConfirmClaimRequest;
import com.picpay.banking.claim.dto.response.ConfirmClaimResponse;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmClaimPortImpl implements ConfirmClaimPort {

    private static final String CIRCUIT_BREAKER_NAME = "confirm-claim-bacen";

    private final BacenClaimClient bacenClaimClient;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "confirmFallback")
    public Claim confirm(Claim claim, ClaimConfirmationReason reason, String requestIdentifier) {

        ConfirmClaimRequest request = ConfirmClaimRequest.from(claim);

        ConfirmClaimResponse response = bacenClaimClient.confirmClaim(claim.getClaimId(), request);

        return response.toClaim();
    }

    public Claim confirmFallback(Claim claim, ClaimConfirmationReason reason, String requestIdentifier, Exception e) {
        log.error("Claim_fallback_confirmBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("claimType", claim.getClaimType()),
                kv("key", claim.getKey()),
                kv("cpfCnpf", claim.getCpfCnpj()),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e).build();
    }

}
