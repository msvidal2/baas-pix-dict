package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.Vsync.VsyncResult;
import com.picpay.banking.pix.core.ports.reconciliation.BacenSyncVerificationsPort;
import org.springframework.stereotype.Component;

@Component
public class BacenSyncVerificationsPortImpl implements BacenSyncVerificationsPort {

    @Override
    public VsyncResult syncVerification(final String vsync) {
        return null;
    }

}
