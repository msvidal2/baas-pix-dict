package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction;

import java.util.Set;

public interface SyncVerifierHistoricActionPort {

    void saveAll(Set<SyncVerifierHistoricAction> syncVerifierHistoricActions);

}
