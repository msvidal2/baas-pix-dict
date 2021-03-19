package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.reconciliation.ContentIdentifierEvent;

public interface ContentIdentifierEventPort {

    void save(ContentIdentifierEvent event);

}