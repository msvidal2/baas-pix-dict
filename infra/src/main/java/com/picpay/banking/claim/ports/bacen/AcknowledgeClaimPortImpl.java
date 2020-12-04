package com.picpay.banking.claim.ports.bacen;

import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.request.AcknowledgeClaimRequest;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.AcknowledgeClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class AcknowledgeClaimPortImpl implements AcknowledgeClaimPort {

    private static final String CIRCUIT_BREAKER_NAME = "acknowledge-claim-bacen";

    private final BacenClaimClient bacenClaimClient;

    private final TimeLimiterExecutor timeLimiterExecutor;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "acknowledgeFallback")
    public Claim acknowledge(String claimId, Integer ispb) {
        var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> bacenClaimClient.acknowledge(claimId, new AcknowledgeClaimRequest(claimId, ispb.toString())),
                null);

        log.info("Claim_acknowledgeBacen",
                kv("claimId", claimId),
                kv("ispb", ispb),
                kv("response", response));

        return response.toClaim();
    }

    public Claim acknowledgeFallback(String claimId, Integer ispb, Exception e) {
        throw BacenExceptionBuilder.from(e).build();
    }

}
