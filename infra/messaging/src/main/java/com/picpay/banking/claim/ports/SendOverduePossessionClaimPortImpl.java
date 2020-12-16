package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.config.ClaimTopicBindingOutput;
import com.picpay.banking.claim.config.OverduePossessionClaimOutputTopic;
import com.picpay.banking.claim.dto.ClaimDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.SendOverduePossessionClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendOverduePossessionClaimPortImpl implements SendOverduePossessionClaimPort {

    private static final String CIRCUIT_BREAKER = "send-overdue-possession-claim";

    private final OverduePossessionClaimOutputTopic overduePossessionClaimOutputTopic;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "sendFallback")
    public void send(Claim claim) {
        overduePossessionClaimOutputTopic
                .getMessageChannel()
                .send(MessageBuilder
                        .withPayload(ClaimDTO.from(claim))
                        .build());
    }

    public void sendFallback(final Claim claim, final Exception e) {
        log.error("SendOverduePossessionClaim_fallback", kv("claimId", claim.getClaimId()));
    }

}
