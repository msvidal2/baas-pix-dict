package com.picpay.banking.pix.core.validators.reconciliation;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.ReconciliationSyncEvent;
import com.picpay.banking.pix.core.exception.InvalidReconciliationSyncEventException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReconciliationSyncEventValidator {

    public static void validate(ReconciliationSyncEvent event) throws InvalidReconciliationSyncEventException {
        if (event.getKeyType() == null) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires that the keyType be reported");
        }

        if (Strings.isNullOrEmpty(event.getKey())) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires the key to be informed");
        }

        if (event.getOperation() == null) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires the type of operation to be informed");
        }

        if (event.getHappenedAt() == null) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires that the date be informed");
        }

        switch (event.getOperation()) {
            case ADD:
                if (Strings.isNullOrEmpty(event.getNewCid())) {
                    throw new InvalidReconciliationSyncEventException(
                        "An ADD reconciliation event requires newCid to be informed");
                }
                break;
            case UPDATE:
                if (Strings.isNullOrEmpty(event.getOldCid())) {
                    log.warn("An UPDATE reconciliation event did not inform oldCid");
                }
                if (Strings.isNullOrEmpty(event.getNewCid())) {
                    throw new InvalidReconciliationSyncEventException(
                        "An UPDATE reconciliation event requires newCid to be informed");
                }
                break;
            case REMOVE:
                if (Strings.isNullOrEmpty(event.getOldCid())) {
                    log.warn("A REMOVE reconciliation event did not inform oldCid");
                }
                if (Strings.isNullOrEmpty(event.getNewCid())) {
                    throw new InvalidReconciliationSyncEventException(
                        "A REMOVE reconciliation event should not inform newCid");
                }
                break;
        }
    }

}
