package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ContentIdentifierEvent {

    private EventType eventType;
    private String cid;
    private LocalDateTime dateTime;

    public enum EventType {
        ADD,
        REMOVE
    }

}
