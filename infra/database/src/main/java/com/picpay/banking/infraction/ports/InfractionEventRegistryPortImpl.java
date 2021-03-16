package com.picpay.banking.infraction.ports;

import com.picpay.banking.infraction.entity.InfractionReportEventEntity;
import com.picpay.banking.infraction.repository.InfractionReportEventRepository;
import com.picpay.banking.pix.core.events.InfractionReportEvent;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionEventRegistryPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportCacheSavePort;
import com.picpay.banking.pix.core.validators.idempotency.annotation.IdempotencyKey;
import com.picpay.banking.pix.core.validators.idempotency.annotation.ValidateIdempotency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfractionEventRegistryPortImpl implements InfractionEventRegistryPort {

    private final InfractionReportEventRepository infractionReportEventRepository;

    private final InfractionReportCacheSavePort infractionReportCacheSavePort;

    @Override
    @ValidateIdempotency(InfractionReportEventData.class)
    public void registry(InfractionReportEvent event, @IdempotencyKey String requestIdentifier, InfractionReportEventData infractionReportEventData) {

        var infractionEventEntity = InfractionReportEventEntity.builder()
                .type(event)
                .infractionReportId(infractionReportEventData.getInfractionReportId())
                .data(infractionReportEventData)
                .creationDate(LocalDateTime.now())
                .requestIdentifier(requestIdentifier)
                .build();

        infractionReportEventRepository.save(infractionEventEntity);

        infractionReportCacheSavePort.save(infractionReportEventData, requestIdentifier);

        log.info("InfractionEventRegistry_registered {} {}",
                kv("event", event),
                kv("requestIdentifier", requestIdentifier),
                kv("infractionReportEvent", infractionReportEventData));
    }

}
