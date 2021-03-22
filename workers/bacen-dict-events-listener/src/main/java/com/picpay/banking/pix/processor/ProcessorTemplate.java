package com.picpay.banking.pix.processor;

import com.picpay.banking.exceptions.BacenException;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.data.ErrorEventData;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.infra.config.StreamConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @author diego.araujo
 * @author rafael.tavares
 * @version 1.0 18/03/2021
 */
@Slf4j
@AllArgsConstructor
@Component
public class ProcessorTemplate {

    @SendTo(StreamConfig.OUTPUT)
    public DomainEvent handle(EventProcessor processor, DomainEvent domainEvent) {
        try {
            return processor.process(domainEvent);
        } catch (BacenException e) {
            if (e.isRetryable())
                throw e;
            return getDomainEventError(domainEvent, processor, ErrorEventDataFactory.fromBacenException(e));
        } catch (UseCaseException e) {
            return getDomainEventError(domainEvent, processor, ErrorEventDataFactory.fromUseCaseException(e));
        } catch (Exception e) {
            return getDomainEventError(domainEvent, processor, ErrorEventDataFactory.fromException(e));
        }
    }

    private DomainEvent getDomainEventError(final DomainEvent domainEvent,
                                            final EventProcessor eventProcessor,
                                            final ErrorEventData errorEventData) {
        return DomainEvent.builder()
                .eventType(eventProcessor.failedEventType())
                .domain(domainEvent.getDomain())
                .requestIdentifier(domainEvent.getRequestIdentifier())
                .source(domainEvent.getSource())
                .errorEvent(errorEventData)
                .build();
    }

}
