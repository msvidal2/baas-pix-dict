package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ContentIdentifierEvent {

    private EventType eventType;
    private String cid;
    private LocalDateTime dateTime;

    public enum EventType {
        ADD,
        REMOVE
    }

}
