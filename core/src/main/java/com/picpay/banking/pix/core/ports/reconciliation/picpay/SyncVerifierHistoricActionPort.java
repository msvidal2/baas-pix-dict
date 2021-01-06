package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction;

public interface SyncVerifierHistoricActionPort {

    void save(SyncVerifierHistoricAction syncVerifierHistoricAction);

}
