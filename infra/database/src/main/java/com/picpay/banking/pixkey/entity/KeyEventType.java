package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pix.core.domain.PixKeyEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum KeyEventType {

    PENDING_CREATE(PixKeyEvent.PENDING_CREATE),
    PENDING_UPDATE(PixKeyEvent.PENDING_UPDATE),
    PENDING_REMOVE(PixKeyEvent.PENDING_REMOVE),
    CREATED(PixKeyEvent.CREATED),
    REMOVED(PixKeyEvent.REMOVED),
    UPDATED(PixKeyEvent.UPDATED);

    private PixKeyEvent event;

    public static KeyEventType resolve(final PixKeyEvent event) {
        return Stream.of(values())
                .filter(v -> v.event.equals(event))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

//    OPEN(0),
//    CREATED(1),
//    REMOVED(2);
//    private final int value;

}
