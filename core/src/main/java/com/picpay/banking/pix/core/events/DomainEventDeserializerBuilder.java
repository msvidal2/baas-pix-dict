package com.picpay.banking.pix.core.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.picpay.banking.pix.core.events.data.ErrorEventData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
public class DomainEventDeserializerBuilder {

    private EventType eventType;
    private Domain domain;
    private Object source;
    private ErrorEventData errorEvent;
    private String requestIdentifier;

    public DomainEventDeserializerBuilder eventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public DomainEventDeserializerBuilder domain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public DomainEventDeserializerBuilder source(Object source) {
        this.source = source;
        return this;
    }

    public DomainEventDeserializerBuilder errorEvent(ErrorEventData errorEvent) {
        this.errorEvent = errorEvent;
        return this;
    }

    public DomainEventDeserializerBuilder requestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
        return this;
    }

    public DomainEvent build() {
        var dateModule = new SimpleModule();
        dateModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        dateModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));

        var mapper = new ObjectMapper();
        mapper.registerModules(dateModule);

        return DomainEvent.builder()
                .eventType(eventType)
                .domain(domain)
                .source(mapper.convertValue(source, eventType.getAClass()))
                .errorEvent(errorEvent)
                .requestIdentifier(requestIdentifier)
                .build();
    }

}
