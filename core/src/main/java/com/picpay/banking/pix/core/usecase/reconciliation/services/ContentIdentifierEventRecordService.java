package com.picpay.banking.pix.core.usecase.reconciliation.services;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.ports.reconciliation.ContentIdentifierEventPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ContentIdentifierEventRecordService {

    private final ContentIdentifierEventPort port;

    public void add(ReconciliationEvent event) { port.save(event); }

    public void update(ReconciliationEvent event) {
        port.update(event);
    }

    public void remove(ReconciliationEvent event) {
        port.remove(event);
    }
}
