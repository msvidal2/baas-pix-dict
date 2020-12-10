package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
public class ContentIdentifierEvent {

    @NonNull
    private final String cid;
    @NonNull
    private final ContentIdentifierEventType contentIdentifierType;
    @NonNull
    private final LocalDateTime eventOnBacenAt;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ContentIdentifierEvent that = (ContentIdentifierEvent) o;
        return cid.equals(that.cid) && contentIdentifierType == that.contentIdentifierType && eventOnBacenAt.equals(that.eventOnBacenAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid, contentIdentifierType, eventOnBacenAt);
    }

    public enum ContentIdentifierEventType {
        ADDED,
        REMOVED
    }

}

