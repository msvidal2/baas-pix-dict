package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifier;

public interface SyncVerifierPort {

    SyncVerifier getLastSuccessfulVsync(KeyType keyType);

    void save(SyncVerifier syncVerifier);

}
