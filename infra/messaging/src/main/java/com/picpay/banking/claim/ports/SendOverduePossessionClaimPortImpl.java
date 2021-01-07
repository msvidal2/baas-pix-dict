package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.config.OverduePossessionClaimClaimerOutputTopic;
import com.picpay.banking.claim.config.OverduePossessionClaimDonorOutputTopic;
import com.picpay.banking.claim.dto.ClaimDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.SendOverduePossessionClaimPort;
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
@ConditionalOnProperty(value = "picpay.polling.overdue-possession.notification-dispatcher", havingValue = "true")
public class SendOverduePossessionClaimPortImpl implements SendOverduePossessionClaimPort {

    private static final String CIRCUIT_BREAKER = "send-overdue-possession-claim";

    private final OverduePossessionClaimDonorOutputTopic overduePossessionClaimDonorOutputTopic;
    private final OverduePossessionClaimClaimerOutputTopic overduePossessionClaimClaimerOutputTopic;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "sendFallback")
    public void sendToConfirm(Claim claim) {
        overduePossessionClaimDonorOutputTopic
                .getMessageChannel()
                .send(MessageBuilder
                        .withPayload(ClaimDTO.from(claim))
                        .build());
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "sendFallback")
    public void sendToCancel(Claim claim) {
        overduePossessionClaimClaimerOutputTopic
                .getMessageChannel()
                .send(MessageBuilder
                        .withPayload(ClaimDTO.from(claim))
                        .build());
    }

    public void sendFallback(final Claim claim, final Exception e) {
        log.error("SendOverduePossessionClaim_fallback", kv("claimId", claim.getClaimId()));
    }

}
