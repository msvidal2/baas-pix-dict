package com.picpay.banking.pix.core.util;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.ReconciliationAction;
import com.picpay.banking.pix.core.domain.ReconciliationEvent;

import java.time.LocalDateTime;

public class ContentIdentifierUtil {

    public static ReconciliationEvent createContentIdentifier(String cid) {
        return ReconciliationEvent.builder()
            .cid(cid)
            .action(ReconciliationAction.ADD)
            .key("")
            .keyType(KeyType.CELLPHONE)
            .eventOnBacenAt(LocalDateTime.now())
            .build();
    }

    public static ReconciliationEvent createContentIdentifier(String cid, ReconciliationAction action) {
        return ReconciliationEvent.builder()
            .cid(cid)
            .action(action)
            .key("")
            .keyType(KeyType.CELLPHONE)
            .eventOnBacenAt(LocalDateTime.now())
            .build();
    }

}
