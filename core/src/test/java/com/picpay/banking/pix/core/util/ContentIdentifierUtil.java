package com.picpay.banking.pix.core.util;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ContentIdentifierUtil {

    public static BacenCidEvent bacenCidEventAdd(String cid) {
        return BacenCidEvent.builder()
            .cid(cid)
            .action(ReconciliationAction.ADDED)
            .eventOnBacenAt(LocalDateTime.now(ZoneId.of("UTC")))
            .build();
    }

    public static BacenCidEvent bacenCidEventRemove(String cid) {
        return BacenCidEvent.builder()
            .cid(cid)
            .action(ReconciliationAction.REMOVED)
            .eventOnBacenAt(LocalDateTime.now(ZoneId.of("UTC")))
            .build();
    }

}
