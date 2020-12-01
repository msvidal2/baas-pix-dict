package com.picpay.banking.reconciliation.ports.bacen;

import com.picpay.banking.pix.core.domain.SyncVerifierResult;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import org.springframework.stereotype.Component;

@Component
public class BacenSyncVerificationsPortImpl implements BacenSyncVerificationsPort {

    @Override
    public SyncVerifierResult syncVerification(final String vsync) {
        // TODO:
        throw new UnsupportedOperationException();
    }

}
