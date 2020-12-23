package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.config.CancelPortabilityPollingOutputBinding;
import com.picpay.banking.claim.dto.ClaimDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToCancelPortabilityPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendToCancelPortabilityPortImpl implements SendToCancelPortabilityPort {

    private static final String CIRCUIT_BREAKER = "send-to-cancel-portability";

    private final CancelPortabilityPollingOutputBinding cancelPortabilityPollingOutputBinding;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "sendFallback")
    public void send(final Claim claim) {
        var message = MessageBuilder
                .withPayload(ClaimDTO.from(claim))
                .build();

        cancelPortabilityPollingOutputBinding.sendNewPortabilityToCancel().send(message);
    }

    public void sendFallback(final Claim claim, final Exception e) {
        log.error("SendToCancelPortability_fallback",
                kv("claimId", claim.getClaimId()));
    }

}
