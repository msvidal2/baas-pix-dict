package com.picpay.banking.pix.core.validators.reconciliation;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.exception.reconciliation.InvalidReconciliationOperationException;


public class ContentIdentifierEventValidator {

    public static void validate(ReconciliationEvent event) throws InvalidReconciliationOperationException {
        String cid = null;

        if (event.isARemoveAction() || event.isAUpdateAction()) {
            if (cid == null)
                throw new InvalidReconciliationOperationException(
                    "The requested action can't be performed because the content identifier does not exist");
        }
    }

}
