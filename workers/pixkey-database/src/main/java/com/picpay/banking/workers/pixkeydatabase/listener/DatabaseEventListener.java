package com.picpay.banking.workers.pixkeydatabase.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pixkey.config.DictEventInputBinding;
import com.picpay.banking.workers.pixkeydatabase.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_CREATED_BACEN;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_REMOVED_BACEN;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_UPDATED_BACEN;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseEventListener {

    private final ObjectMapper objectMapper;
    private final DatabaseService databaseService;

    @Bean
    public Predicate<Object> databaseEventsListenerFilterCondition() {
        List<EventType> filterEventType = List.of(PIX_KEY_CREATED_BACEN, PIX_KEY_UPDATED_BACEN, PIX_KEY_REMOVED_BACEN);
        return bytes -> {
            try {
                var domainEvent = objectMapper.readValue((byte[]) bytes, DomainEvent.class);
                return domainEvent.getDomain() == Domain.PIX_KEY && filterEventType.contains(domainEvent.getEventType());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        };
    }

    @StreamListener(value = DictEventInputBinding.INPUT, condition = "@databaseEventsListenerFilterCondition.test(payload)")
    //    @StreamListener(value = DictEventInputBinding.INPUT)
    public void processDatabaseEvent(Message<DomainEvent<PixKeyEventData>> message) {
        var pixKeyMessage = message.getPayload();
        var pixKeyEventData = objectMapper.convertValue(pixKeyMessage.getSource(), PixKeyEventData.class);

        switch (pixKeyMessage.getEventType()) {
            case PIX_KEY_CREATED_BACEN:
                databaseService.create(pixKeyEventData);
                break;
            case PIX_KEY_UPDATED_BACEN:
                databaseService.update(pixKeyEventData);
                break;
            case PIX_KEY_REMOVED_BACEN:
                databaseService.remove(pixKeyEventData);
                break;
            default:
                error(pixKeyMessage, pixKeyEventData);
        }
    }

    private void error(final DomainEvent<PixKeyEventData> domainEvent, final PixKeyEventData pixKeyEventData) {
        log.error("O tipo de evento {} é inválido. {} {}", domainEvent.getEventType().name(),
            kv("pixKeyEvent", domainEvent.getEventType()),
            kv("pixKeyEventData", pixKeyEventData));
        throw new UnsupportedOperationException(String.format("O tipo do evento %s é inválido", domainEvent.getEventType().name()));
    }

}