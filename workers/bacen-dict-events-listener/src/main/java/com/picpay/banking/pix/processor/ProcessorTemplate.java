/*
 *  baas-pix-dict 1.0 18/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.processor;

import com.picpay.banking.exceptions.BacenException;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.ErrorEventData;
import com.picpay.banking.pix.core.events.data.FieldData;
import com.picpay.banking.pix.infra.config.StreamConfig;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rafael.braga
 * @author diego.araujo
 * @version 1.0 18/03/2021
 */
public abstract class ProcessorTemplate<T> implements EventProcessor<T> {

    protected abstract EventType failedEventType();

    protected abstract DomainEvent<T> handle(final DomainEvent<T> domainEvent);

    @SendTo(StreamConfig.OUTPUT)
    public DomainEvent<T> process(final DomainEvent<T> domainEvent) {
        try {
            return handle(domainEvent);
        } catch (BacenException e) {
            if (e.isRetryable())
                throw e;

            return getDomainEvent(domainEvent, getErrorEventDataFromBacenException(e));
        } catch (Exception e) {
            return getDomainEvent(domainEvent, failedEvent(e));
        }
    }

    public DomainEvent<T> getDomainEvent(final DomainEvent<T> domainEvent, final ErrorEventData errorEventData) {
        return DomainEvent.<T>builder()
                .eventType(failedEventType())
                .domain(domainEvent.getDomain())
                .requestIdentifier(domainEvent.getRequestIdentifier())
                .source(domainEvent.getSource())
                .errorEvent(errorEventData)
                .build();
    }

    public ErrorEventData getErrorEventDataFromBacenException(BacenException e) {
        var bacenError = e.getBacenError();

        var fields = Optional.ofNullable(bacenError.getFields())
                .orElse(Collections.emptyList())
                .stream()
                .map(f -> FieldData.builder()
                        .message(f.getDefaultMessage())
                        .property(f.getField())
//                        .value(f.) // TODO: implementar get value
                        .build())
                .collect(Collectors.toList());

        return ErrorEventData.builder()
                .code(bacenError.getMessage())
                .description(bacenError.getDetail())
                .fields(fields)
                .build();
    }

}