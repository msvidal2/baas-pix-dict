package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.PixKeyEvent;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.events.data.PixKeyMessage;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import com.picpay.banking.pixkey.config.PixKeyEventOutputBinding;
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

    private final PixKeyEventOutputBinding pixKeyEventOutputBinding;

    @Override
    public void registry(PixKeyEvent event, String requestIdentifier, PixKeyEventData pixKeyEventData, Reason reason) {

        var message = MessageBuilder
                .withPayload(PixKeyMessage.builder()
                        .event(event)
                        .data(pixKeyEventData)
                        .requestIdentifier(requestIdentifier)
                        .reason(reason)
                        .build())
                .build();

        var result = pixKeyEventOutputBinding.output().send(message, 2000);

        log.info("PixKeyEventRegistry_sent {} {}",
                kv("event", event),
                kv("requestIdentifier", requestIdentifier),
                kv("result", result));
    }

}
