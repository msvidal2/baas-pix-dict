package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Builder
@Getter
public class ContentIdentifierEvent {

    @NonNull
    private String cid;
    private String key;
    private KeyType keyType;
    @NonNull
    private ContentIdentifierEventType contentIdentifierType;
    @NonNull
    private LocalDateTime keyOwnershipDate;

    public enum ContentIdentifierEventType {
        ADD,
        REMOVE
    }

}

