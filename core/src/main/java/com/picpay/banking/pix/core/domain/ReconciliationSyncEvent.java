package com.picpay.banking.pix.core.domain;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.exception.InvalidReconciliationSyncEventException;
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
    private final DictAction action;
    private final LocalDateTime happenedAt;
    private final String newCid;
    private final String oldCid;

    public List<ReconciliationEvent> generateContentIdentifierEvents() {
        List<ReconciliationEvent> events = new ArrayList<>();

        switch (action) {
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

    public List<ReconciliationEvent> toDomain() {
        List<ReconciliationEvent> domainEvents = new ArrayList<>();
        switch (this.getAction()) {
            case ADD: domainEvents.add(createAddReconciliationEvent());
            break;
            case REMOVE: domainEvents.add(createRemoveReconciliationEvent());
            break;
            case UPDATE:
                domainEvents.add(createRemoveReconciliationEvent());
                domainEvents.add(createAddReconciliationEvent());
        }
        return domainEvents;
    }

    public void validate() {
        if (this.getKeyType() == null) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires that the keyType be reported");
        }

        if (Strings.isNullOrEmpty(this.getKey())) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires the key to be informed");
        }

        if (this.getAction() == null) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires the type of operation to be informed");
        }

        if (this.getHappenedAt() == null) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires that the date be informed");
        }

        switch (this.getAction()) {
            case ADD:
                if (Strings.isNullOrEmpty(this.getNewCid())) {
                    throw new InvalidReconciliationSyncEventException(
                            "An ADD reconciliation event requires newCid to be informed");
                }
                break;
            case UPDATE:
                if (Strings.isNullOrEmpty(this.getOldCid())) {
                    throw new InvalidReconciliationSyncEventException("An UPDATE reconciliation event did not inform oldCid");
                }
                if (Strings.isNullOrEmpty(this.getNewCid())) {
                    throw new InvalidReconciliationSyncEventException(
                            "An UPDATE reconciliation event requires newCid to be informed");
                }
                break;
            case REMOVE:
                if (Strings.isNullOrEmpty(this.getOldCid())) {
                    throw new InvalidReconciliationSyncEventException("A REMOVE reconciliation event did not inform oldCid");
                }
                if (Strings.isNullOrEmpty(this.getNewCid())) {
                    throw new InvalidReconciliationSyncEventException(
                            "A REMOVE reconciliation event should not inform newCid");
                }
                break;
        }
    }

}

