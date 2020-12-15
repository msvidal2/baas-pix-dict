package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.config.ClaimTopicBindingOutput;
import com.picpay.banking.claim.dto.ClaimDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToProcessClaimNotificationPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendToProcessClaimNotificationPortImpl implements SendToProcessClaimNotificationPort {

    private static final String CIRCUIT_BREAKER = "send-to-process-claim-notification";

    private final ClaimTopicBindingOutput claimTopicBindingOutput;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "sendFallback")
    public void send(final Claim claim) {
        var message = MessageBuilder
                .withPayload(ClaimDTO.from(claim))
                .build();

        claimTopicBindingOutput.getClaimNotificationsOutput().send(message);
    }

    public void sendFallback(final Claim claim, final Exception e) {
        log.error("SendToProcessClaimNotification_fallback",
                kv("claimId", claim.getClaimId()));
    }

}
