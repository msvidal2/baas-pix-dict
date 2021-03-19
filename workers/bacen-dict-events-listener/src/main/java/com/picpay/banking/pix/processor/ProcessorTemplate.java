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
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.infra.config.StreamConfig;
import org.springframework.messaging.handler.annotation.SendTo;

/**
 * @author rafael.braga
 * @author diego.araujo
 * @author rafael.tavares
 * @version 1.0 18/03/2021
 */
public abstract class ProcessorTemplate<T> implements EventProcessor<T> {

    @SendTo(StreamConfig.OUTPUT)
    public DomainEvent<T> process(final DomainEvent<T> domainEvent) {
        try {
            return handle(domainEvent);
        } catch (BacenException e) {
            if (e.isRetryable())
                throw e;

            return getDomainEventError(domainEvent, ErrorEventDataFactory.fromBacenException(e));
        } catch (UseCaseException e) {
            return getDomainEventError(domainEvent, ErrorEventDataFactory.fromUseCaseException(e));
        } catch (Exception e) {
            return getDomainEventError(domainEvent, ErrorEventDataFactory.fromException(e));
        }
    }

    protected abstract DomainEvent<T> handle(final DomainEvent<T> domainEvent);

    protected abstract EventType failedEventType();

    private DomainEvent<T> getDomainEventError(final DomainEvent<T> domainEvent, final ErrorEventData errorEventData) {
        return DomainEvent.<T>builder()
                .eventType(failedEventType())
                .domain(domainEvent.getDomain())
                .requestIdentifier(domainEvent.getRequestIdentifier())
                .source(domainEvent.getSource())
                .errorEvent(errorEventData)
                .build();
    }

}