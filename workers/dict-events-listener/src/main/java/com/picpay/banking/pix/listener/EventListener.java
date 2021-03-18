/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.listener;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventKey;
import com.picpay.banking.pix.core.events.EventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Component
@RequiredArgsConstructor
public class EventListener {

    private final Map<EventKey, EventProcessor> processors;

    @StreamListener("dict-events")
    public void listen(DomainEvent domainEvent) {
        var eventKey = EventKey.builder().eventType(domainEvent.getEventType()).domain(domainEvent.getDomain()).build();
        EventProcessor processor = processors.get(eventKey);
        processor.process(domainEvent);
    }

}
