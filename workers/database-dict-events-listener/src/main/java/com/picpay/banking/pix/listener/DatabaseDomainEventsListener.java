/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.listener;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventKey;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.infra.config.DomainEventsStreamConfig;
import com.picpay.banking.pix.processor.ProcessorTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseDomainEventsListener {

    private final Map<EventKey, Optional<EventProcessor>> processors;
    private final ProcessorTemplate template;

    @StreamListener(DomainEventsStreamConfig.DOMAIN_DATABASE_EVENTS)
    public void listen(DomainEvent domainEvent) {

        var eventKey = EventKey.builder()
            .eventType(domainEvent.getEventType())
            .domain(domainEvent.getDomain())
            .build();

        Optional<EventProcessor> eventProcessor = processors.getOrDefault(eventKey, Optional.empty());

        eventProcessor.ifPresentOrElse(
            proc -> template.handle(proc, domainEvent),
            () -> log.info("Processador não encontrado para tipo de evento {}-{}", eventKey.getDomain(), eventKey.getEventType()));
    }

}
