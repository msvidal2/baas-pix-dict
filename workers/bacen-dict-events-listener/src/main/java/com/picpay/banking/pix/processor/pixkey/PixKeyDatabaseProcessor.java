/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.processor.pixkey;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.CreateDatabasePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemoveDatabasePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateDatabasePixKeyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component(value = "pixKeyDatabaseProcessor")
public class PixKeyDatabaseProcessor implements EventProcessor<PixKeyEventData> {

    private final CreateDatabasePixKeyUseCase createDatabasePixKeyUseCase;
    private final UpdateDatabasePixKeyUseCase updateDatabasePixKeyUseCase;
    private final RemoveDatabasePixKeyUseCase removeDatabasePixKeyUseCase;

    @Override
    @Transactional
    public DomainEvent<PixKeyEventData> process(final DomainEvent<PixKeyEventData> domainEvent) {
        switch (domainEvent.getEventType()) {
            case PIX_KEY_CREATED_BACEN:
                createDatabasePixKeyUseCase.execute(domainEvent.getSource());
                break;
            case PIX_KEY_UPDATED_BACEN:
                updateDatabasePixKeyUseCase.execute(domainEvent.getSource());
                break;
            case PIX_KEY_REMOVED_BACEN:
                removeDatabasePixKeyUseCase.execute(domainEvent.getSource());
                break;
            default:
                error(domainEvent);
        }

        return null;
    }

    private void error(final DomainEvent<PixKeyEventData> domainEvent) {
        log.error("O tipo de evento {} é inválido. {} {}", domainEvent.getEventType().name(),
            kv("eventType", domainEvent.getEventType()),
            kv("source", domainEvent.getSource()));
        throw new UnsupportedOperationException(String.format("O tipo do evento %s é inválido", domainEvent.getEventType().name()));
    }

}
