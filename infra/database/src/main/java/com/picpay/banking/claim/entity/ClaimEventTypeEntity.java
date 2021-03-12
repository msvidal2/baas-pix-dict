package com.picpay.banking.claim.entity;

import com.picpay.banking.pix.core.domain.ClaimEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ClaimEventTypeEntity {

    OPEN(ClaimEventType.OPEN),
    AWAITING_CLAIM(ClaimEventType.AWAITING_CLAIM),
    CONFIRMED(ClaimEventType.CONFIRMED),
    CANCELED(ClaimEventType.CANCELED),
    COMPLETED(ClaimEventType.COMPLETED),
    PENDING_OPEN(ClaimEventType.PENDING_OPEN),
    PENDING_AWAITING_CLAIM(ClaimEventType.PENDING_AWAITING_CLAIM),
    PENDING_CONFIRMED(ClaimEventType.PENDING_CONFIRMED),
    PENDING_CANCELED(ClaimEventType.PENDING_CANCELED),
    PENDING_COMPLETED(ClaimEventType.PENDING_COMPLETED);

    private ClaimEventType event;

    public static ClaimEventTypeEntity resolve(final ClaimEventType event) {
        return Stream.of(values())
                .filter(v -> v.event.equals(event))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}