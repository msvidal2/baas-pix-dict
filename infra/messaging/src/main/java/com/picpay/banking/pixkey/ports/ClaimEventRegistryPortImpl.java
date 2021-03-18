package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.ClaimEventData;
import com.picpay.banking.pix.core.ports.claim.ClaimEventRegistryPort;
import com.picpay.banking.pix.core.ports.claim.picpay.ClaimCacheSavePort;
import com.picpay.banking.pix.core.validators.idempotency.annotation.IdempotencyKey;
import com.picpay.banking.pix.core.validators.idempotency.annotation.ValidateIdempotency;
import com.picpay.banking.pixkey.config.DictEventOutputBinding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "picpay.dict.claim.event-registry", havingValue = "true")
public class ClaimEventRegistryPortImpl implements ClaimEventRegistryPort {

    private final DictEventOutputBinding dictEventOutputBinding;

    private final ClaimCacheSavePort claimCacheSavePort;

    @Override
    @ValidateIdempotency(ClaimEventData.class)
    public void registry(@IdempotencyKey String requestIdentifier, EventType event, ClaimEventData claimEventData) {

        var message = MessageBuilder
                .withPayload(DomainEvent.<ClaimEventData>builder()
                        .eventType(event)
                        .domain(Domain.CLAIM)
                        .source(claimEventData)
                        .requestIdentifier(requestIdentifier)
                        .build())
                .build();

        var result = dictEventOutputBinding.output().send(message, 2000);

        claimCacheSavePort.save(claimEventData, requestIdentifier);

        log.info("ClaimEventRegistry_sent {} {}",
                kv("event", event),
                kv("requestIdentifier", requestIdentifier),
                kv("result", result));
    }

}
