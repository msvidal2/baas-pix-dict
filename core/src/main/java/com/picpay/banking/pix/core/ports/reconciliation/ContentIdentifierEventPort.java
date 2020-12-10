package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;

public interface ContentIdentifierEventPort {

    void save(ReconciliationEvent event);

    void update(ReconciliationEvent event);

    void remove(ReconciliationEvent event);

    ReconciliationEvent find(ReconciliationEvent event);
}
