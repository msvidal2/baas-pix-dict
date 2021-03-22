/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.listener;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventKey;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.infra.config.StreamConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BacenEventsListener {

    private final Map<EventKey, Optional<EventProcessor>> processors;

    @StreamListener(StreamConfig.INPUT)
    public void listen(DomainEvent domainEvent) {

        var eventKey = EventKey.builder()
                .eventType(domainEvent.getEventType())
                .domain(domainEvent.getDomain())
                .build();

        Optional<EventProcessor> eventProcessor = processors.getOrDefault(eventKey, Optional.empty());

        eventProcessor.ifPresentOrElse(proc -> proc.process(domainEvent),
                                       () -> log.info("Processador não encontrado para tipo de evento {}-{}", eventKey.getDomain(), eventKey.getEventType()));
    }

}
