package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.events.PixKeyEvent;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyCacheSavePort;
import com.picpay.banking.pix.core.validators.idempotency.annotation.IdempotencyKey;
import com.picpay.banking.pix.core.validators.idempotency.annotation.ValidateIdempotency;
import com.picpay.banking.pixkey.entity.PixKeyEventEntity;
import com.picpay.banking.pixkey.repository.PixKeyEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class PixKeyEventRegistryPortImpl implements PixKeyEventRegistryPort {

    private final PixKeyEventRepository eventRepository;

    private final PixKeyCacheSavePort pixKeyCacheSavePort;

    @Override
    @ValidateIdempotency(PixKey.class)
    public void registry(final PixKeyEvent event, @IdempotencyKey final String requestIdentifier, final PixKeyEventData pixKey, final Reason reason) {

        var eventEntity = PixKeyEventEntity.builder()
                .type(event)
                .requestIdentifier(requestIdentifier)
                .pixKey(pixKey.getKey())
                .pixKeyType(pixKey.getType())
                .data(pixKey)
                .creationDate(LocalDateTime.now())
                .build();

        eventRepository.save(eventEntity);

        pixKeyCacheSavePort.save(pixKey, requestIdentifier);

        log.info("PixKeyEventRegistry_registered {} {}",
                kv("event", event),
                kv("requestIdentifier", requestIdentifier),
                kv("pixKey", pixKey),
                kv("reason", reason));
    }

}
