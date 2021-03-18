package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionEventRegistryPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportCacheSavePort;
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
@ConditionalOnProperty(value = "picpay.dict.infraction.event-registry", havingValue = "true")
public class InfractionEventRegistryPortImpl implements InfractionEventRegistryPort {

    private final DictEventOutputBinding dictEventOutputBinding;

    private final InfractionReportCacheSavePort infractionReportCacheSavePort;

    @Override
    @ValidateIdempotency(InfractionReportEventData.class)
    public void registry(EventType event, @IdempotencyKey String requestIdentifier, InfractionReportEventData infractionReportEventData) {

        var message = MessageBuilder
                .withPayload(DomainEvent.<InfractionReportEventData>builder()
                        .eventType(event)
                        .domain(Domain.INFRACTION_REPORT)
                        .source(infractionReportEventData)
                        .requestIdentifier(requestIdentifier)
                        .build())
                .build();

        var result = dictEventOutputBinding.output().send(message, 2000);

        infractionReportCacheSavePort.save(infractionReportEventData, requestIdentifier);

        log.info("InfractionEventRegistry_registered {} {}",
                kv("event", event),
                kv("requestIdentifier", requestIdentifier),
                kv("result", result));
    }

}
