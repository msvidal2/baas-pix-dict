package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeyEvent;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import com.picpay.banking.pixkey.entity.KeyEventType;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.entity.PixKeyEventEntity;
import com.picpay.banking.pixkey.repository.PixKeyEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class PixKeyEventRegistryPortImpl implements PixKeyEventRegistryPort {

//    private final PixKeyEventRepository eventRepository;

    @Override
    public void registry(final PixKeyEvent event, final String requestIdentifier, final PixKey pixKey, final Reason reason) {

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
