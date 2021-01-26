package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.config.ClaimNotificationOutputBinding;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.SendClaimNotificationPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(value = "picpay.polling.claim.notification-dispatcher", havingValue = "true")
public class SendClaimNotificationPortImpl implements SendClaimNotificationPort {

    private static final String CIRCUIT_BREAKER = "send-new-claim-notification";
    private final ClaimNotificationOutputBinding claimNotificationOutputBinding;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "sendFallback")
    public void send(Claim claim) {
        var message = MessageBuilder.withPayload(claim).build();

        claimNotificationOutputBinding
                .sendNewClaimNotification()
                .send(message);
    }

    public void sendFallback(final Claim claim, final Exception e) {
        log.error("Claim_sendNewClaimNotification_fallback",
                kv("claimId", claim.getClaimId()),
                kv("exception", e));
    }

}