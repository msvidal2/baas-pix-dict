package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeyEvent;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import com.picpay.banking.pix.core.validators.idempotency.annotation.IdempotencyKey;
import com.picpay.banking.pix.core.validators.idempotency.annotation.ValidateIdempotency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class PixKeyEventRegistryPortImpl implements PixKeyEventRegistryPort {

//    private final PixKeyEventRepository eventRepository;

    @ValidateIdempotency(PixKey.class)
    public void registry(final PixKeyEvent event, @IdempotencyKey final String requestIdentifier, final PixKey pixKey, final Reason reason) {

//        var keyEntity = PixKeyEntity.from(key, reason);
//
//        var eventEntity = PixKeyEventEntity.builder()
//                .type(KeyEventType.resolve(event))
//                .pixKey(keyEntity)
//                .data(keyEntity)
//                .build();
//
//        eventRepository.save(eventEntity);

        log.info("REGISTROU EVENTO {} {} {} {}",
                kv("event", event),
                kv("requestIdentifier", requestIdentifier),
                kv("pixKey", pixKey),
                kv("reason", reason));
    }

}
