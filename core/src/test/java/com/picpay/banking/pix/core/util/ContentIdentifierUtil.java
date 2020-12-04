package com.picpay.banking.pix.core.util;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierEvent.ContentIdentifierEventType;

import java.time.LocalDateTime;

public class ContentIdentifierUtil {

    public static ContentIdentifierEvent createContentIdentifier(String cid) {
        return ContentIdentifierEvent.builder()
            .cid(cid)
            .contentIdentifierType(ContentIdentifierEventType.ADDED)
            .keyOwnershipDate(LocalDateTime.now())
            .build();
    }

    public static ContentIdentifierEvent createContentIdentifier(String cid, ContentIdentifierEventType eventType) {
        return ContentIdentifierEvent.builder()
            .cid(cid)
            .contentIdentifierType(eventType)
            .keyOwnershipDate(LocalDateTime.now())
            .build();
    }

}
