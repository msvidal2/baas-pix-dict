package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.claim.dto.response.ClaimStatus;
import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompleteClaimPortImpl implements CompleteClaimPort {

    private static final String CIRCUIT_BREAKER_NAME = "complete-claim-db";

    private final ClaimRepository claimRepository;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "completeClaimFallback")
    public Claim complete(Claim claim, String requestIdentifier) {
        ClaimEntity entity = claimRepository.findClaimerClaimById(claim.getClaimId(), claim.getIspb());
        entity.setStatus(ClaimStatus.COMPLETED);
        claimRepository.save(entity);

        return entity.toClaim();
    }

    public Claim completeClaimFallback(Claim claim, String requestIdentifier, Exception e) {
        log.error("Claim_fallback_completeDB",
                kv("claimId", claim.getClaimId()),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw new RuntimeException();
    }
}
