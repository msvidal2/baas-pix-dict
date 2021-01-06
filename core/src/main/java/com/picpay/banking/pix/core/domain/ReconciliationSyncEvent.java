package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
public class ReconciliationSyncEvent {

    private final KeyType keyType;
    private final String key;
    private final ReconciliationSyncOperation operation;
    private final LocalDateTime happenedAt;
    private final String newCid;
    private final String oldCid;

    public List<ReconciliationEvent> generateContentIdentifierEvents() {
        List<ReconciliationEvent> events = new ArrayList<>();

        switch (operation) {
            case ADD:
                events.add(createAddReconciliationEvent());
                break;
            case UPDATE:
                if (oldCid != null) {
                    events.add(createRemoveReconciliationEvent());
                }
                events.add(createAddReconciliationEvent());
                break;
            case REMOVE:
                if (oldCid != null) {
                    events.add(createRemoveReconciliationEvent());
                }
        }

        return events;
    }

    private ReconciliationEvent createAddReconciliationEvent() {
        return ReconciliationEvent.builder()
            .keyType(keyType)
            .key(key)
            .cid(newCid)
            .action(ReconciliationAction.ADDED)
            .eventOnBacenAt(happenedAt)
            .build();
    }

    private ReconciliationEvent createRemoveReconciliationEvent() {
        return ReconciliationEvent.builder()
            .keyType(keyType)
            .key(key)
            .cid(oldCid)
            .action(ReconciliationAction.REMOVED)
            .eventOnBacenAt(happenedAt)
            .build();
    }

    public enum ReconciliationSyncOperation {
        ADD,
        UPDATE,
        REMOVE;
    }

}

