package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifier;

import java.util.Optional;

public interface SyncVerifierPort {

    Optional<SyncVerifier> getLastSuccessfulVsync(KeyType keyType);

    void update(SyncVerifier syncVerifier);

}
