package com.picpay.banking.claim.ports;

import com.google.common.base.Strings;
import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.ClaimReason;
import com.picpay.banking.claim.dto.request.CancelClaimRequest;
import com.picpay.banking.config.TimeLimiterExecutor;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelClaimBacenPortImpl implements CancelClaimBacenPort {

    private static final String CIRCUIT_BREAKER_NAME = "cancel-claim-bacen";

    private final BacenClaimClient bacenClaimClient;

    private final TimeLimiterExecutor timeLimiterExecutor;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "cancelFallback")
    public Claim cancel(String claimId, ClaimCancelReason reason, int ispb, String requestIdentifier) {
        var request = CancelClaimRequest.builder()
                .claimId(claimId)
                .participant(Strings.padStart(String.valueOf(ispb), 8, '0'))
                .reason(ClaimReason.from(reason))
                .build();

        var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> bacenClaimClient.cancel(claimId, request),
                requestIdentifier);

        log.info("Claim_cancelingBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimId),
                kv("response", response));

        return response.toClaim();
    }

    public Claim cancelFallback(String claimId, ClaimCancelReason reason, int ispb, String requestIdentifier, Exception e) {
        log.error("Claim_fallback_cancelingBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e).build();
    }

}
