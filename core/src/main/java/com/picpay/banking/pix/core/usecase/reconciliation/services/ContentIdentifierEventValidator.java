package com.picpay.banking.pix.core.usecase.reconciliation.services;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.exception.reconciliation.InvalidReconciliationOperationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ContentIdentifierEventValidator {

//    private final ContentIdentifierEventPort port; // trocar porta para pixKey

    public void validate(ReconciliationEvent event) throws Exception {
        // TODO: find cid on pixKey
        String cid = null;

        if (event.isARemoveAction() || event.isAUpdateAction()) {
            if (cid == null)
                throw new InvalidReconciliationOperationException("The requested action can't be performed because the content identifier does not exist");
        }
    }
}
