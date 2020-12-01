package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.ContentIdentifierAction;

import java.util.Set;

public interface ContentIdentifierActionPort {

    void saveAll(Set<ContentIdentifierAction> contentIdentifierActions);

}
