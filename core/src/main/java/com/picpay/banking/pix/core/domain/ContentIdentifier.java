package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Builder
@Getter
public class ContentIdentifier {

    @NonNull
    private String cid;
    private String key;
    private KeyType keyType;
    @NonNull
    private ContentIdentifierType contentIdentifierType;
    @NonNull
    private LocalDateTime dateTime;

    public enum ContentIdentifierType {
        ADD,
        REMOVE;
    }

}
