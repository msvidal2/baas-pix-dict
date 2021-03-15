package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoricAction;

public interface ReconciliationActionPort {

    void insertPixKey(SyncVerifierHistoricAction pixKeyAction);

    void updatePixKey(SyncVerifierHistoricAction oldPixKeyAction, SyncVerifierHistoricAction newPixKeyAction);

    void removePixKey(SyncVerifierHistoricAction pixKeyAction);

}
