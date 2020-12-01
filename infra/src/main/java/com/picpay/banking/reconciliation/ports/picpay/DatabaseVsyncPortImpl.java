package com.picpay.banking.reconciliation.ports.picpay;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifier;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseVsyncPortImpl implements SyncVerifierPort {

    @Override
    public Optional<SyncVerifier> getLastSuccessfulVsync(final KeyType keyType) {
        return Optional.empty();
    }

    @Override
    public void update(final SyncVerifier syncVerifier) {
        // TODO:
        throw new UnsupportedOperationException();
    }

}
