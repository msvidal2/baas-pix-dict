package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;

public interface SyncVerifierHistoricPort {

    SyncVerifierHistoric save(SyncVerifierHistoric syncVerifierHistoric);

}
