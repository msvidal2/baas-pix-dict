package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class FindClaimPortImpl implements FindClaimPort {

    private static final String CIRCUIT_BREAKER_NAME = "find-claim-db";

    private final ClaimRepository claimRepository;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findClaimFallback")
    public Optional<Claim> findClaim(String claimId, String ispb, boolean reivindicador) {
        if (reivindicador) {
            return getClaim(claimRepository.findClaimerClaimById(claimId, ispb));
        }
        return getClaim(claimRepository.findDonorClaimById(claimId, ispb));
    }

    private Optional<Claim> getClaim(ClaimEntity claimEntity) {
        return Optional.ofNullable(claimEntity)
                .map(ClaimEntity::toClaim);
    }

    public Optional<Claim> findClaimFallback(String claimId, String ispb, boolean reivindicador, Exception e) {
        log.error("Claim_fallback_findDB",
                kv("claimId", claimId),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw new RuntimeException();
    }

}
