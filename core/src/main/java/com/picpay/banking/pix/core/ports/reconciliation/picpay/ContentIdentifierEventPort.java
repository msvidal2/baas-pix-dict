package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationEvent;

public interface ContentIdentifierEventPort {

    void save(ReconciliationEvent event);

}
