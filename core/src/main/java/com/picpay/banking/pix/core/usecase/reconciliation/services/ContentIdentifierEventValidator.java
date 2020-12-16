package com.picpay.banking.pix.core.usecase.reconciliation.services;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.exception.reconciliation.InvalidReconciliationOperationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ContentIdentifierEventValidator {

    public void validate(ReconciliationEvent event) throws Exception {
        String cid = null;

        if (event.isARemoveAction() || event.isAUpdateAction()) {
            if (cid == null)
                throw new InvalidReconciliationOperationException("The requested action can't be performed because the content identifier does not exist");
        }
    }
}
