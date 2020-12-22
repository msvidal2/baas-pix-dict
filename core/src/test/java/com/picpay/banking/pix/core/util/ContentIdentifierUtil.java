package com.picpay.banking.pix.core.util;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.ReconciliationAction;
import com.picpay.banking.pix.core.domain.ReconciliationEvent;

import java.time.LocalDateTime;

public class ContentIdentifierUtil {

    public static BacenCidEvent BACEN_CID_EVENT_ADD(String cid) {
        return BacenCidEvent.builder()
            .cid(cid)
            .action(ReconciliationAction.ADDED)
            .eventOnBacenAt(LocalDateTime.now())
            .build();
    }

    public static BacenCidEvent BACEN_CID_EVENT_REMOVE(String cid) {
        return BacenCidEvent.builder()
            .cid(cid)
            .action(ReconciliationAction.REMOVED)
            .eventOnBacenAt(LocalDateTime.now())
            .build();
    }

    public static ReconciliationEvent DATABASE_CID_EVENT_ADD(String cid) {
        return ReconciliationEvent.builder()
            .cid(cid)
            .action(ReconciliationAction.ADDED)
            .eventOnBacenAt(LocalDateTime.now())
            .build();
    }

    public static ReconciliationEvent DATABASE_CID_EVENT_REMOVE(String cid) {
        return ReconciliationEvent.builder()
            .cid(cid)
            .action(ReconciliationAction.REMOVED)
            .eventOnBacenAt(LocalDateTime.now())
            .build();
    }

}
