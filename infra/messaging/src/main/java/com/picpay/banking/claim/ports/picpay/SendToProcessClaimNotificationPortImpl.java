package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.config.ClaimTopicBinding;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.SendToProcessClaimNotificationPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class SendToProcessClaimNotificationPortImpl implements SendToProcessClaimNotificationPort {

    private static final String CIRCUIT_BREAKER = "send-to-process-claim-notification";

    private final ClaimTopicBinding claimTopicBinding;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "sendFallback")
    public void send(final Claim claim) {
        var message = MessageBuilder.withPayload(claim).build();

        claimTopicBinding.getClaimNotificationsOutput().send(message);
    }

    public void sendFallback(final Claim claim, final Exception e) {
        log.error("SendToProcessClaimNotification_fallback",
                kv("claimId", claim.getClaimId()));
    }

}
