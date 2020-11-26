package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateClaimPortImpl implements CreateClaimPort {

    private static final String CIRCUIT_BREAKER_NAME = "create-claim-db";

    private final ClaimRepository claimRepository;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "saveClaimFallback")
    public Claim saveClaim(Claim claim, String requestIdentifier) {

        ClaimEntity entity = ClaimEntity.from(claim);

        ClaimEntity savedEntity = claimRepository.save(entity);

        return savedEntity.toClaim();
    }

    public Claim saveClaimFallback(Claim claim, String requestIdentifier, Exception e) {
        log.error("Claim_fallback_createDB",
                kv("claimId", claim.getClaimId()),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw new RuntimeException();
    }

}
