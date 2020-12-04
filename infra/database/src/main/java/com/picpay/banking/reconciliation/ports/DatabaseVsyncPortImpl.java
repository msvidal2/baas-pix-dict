package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Vsync;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseVsyncPort;
import org.springframework.stereotype.Component;

@Component
public class DatabaseVsyncPortImpl implements DatabaseVsyncPort {


    @Override
    public Vsync getLastSuccessfulVsync(final KeyType keyType) {
        return null;
    }

    @Override
    public void updateVerificationResult(final Vsync vsync) {

    }

}
