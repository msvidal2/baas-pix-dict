package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
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
@ConditionalOnProperty(value = "picpay.dict.pixkey.event-registry", havingValue = "true")
public class PixKeyEventRegistryPortImpl implements PixKeyEventRegistryPort {

    private final DictEventOutputBinding pixKeyEventOutputBinding;

    @Override
    public void registry(EventType event, String requestIdentifier, PixKeyEventData pixKeyEventData) {

        var message = MessageBuilder
                .withPayload(DomainEvent.<PixKeyEventData>builder()
                        .eventType(event)
                        .domain(Domain.PIX_KEY)
                        .source(pixKeyEventData)
                        .requestIdentifier(requestIdentifier)
                        .build())
                .build();

        var result = pixKeyEventOutputBinding.output().send(message, 2000);

        log.info("PixKeyEventRegistry_sent {} {}",
                kv("event", event),
                kv("requestIdentifier", requestIdentifier),
                kv("result", result));
    }

}
