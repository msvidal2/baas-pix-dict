package com.picpay.banking.pix.core.domain.reconciliation;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.exception.InvalidReconciliationSyncEventException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"cid", "action", "eventOnBacenAt"})
public class ContentIdentifierEvent {

    private final String key;

    private final KeyType keyType;

    private final String cid;

    private final ReconciliationAction action;

    private final LocalDateTime eventOnBacenAt;

    public void validate() {
        if (keyType == null) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires that the keyType be reported");
        }

        if (Strings.isNullOrEmpty(key)) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires the key to be informed");
        }

        if (action == null) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires the type of action to be informed");
        }

        if (eventOnBacenAt == null) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires that the eventOnBacenAt be informed");
        }

        if (Strings.isNullOrEmpty(cid)) {
            throw new InvalidReconciliationSyncEventException("Reconciliation requires that the cid be informed");
        }
    }

}
