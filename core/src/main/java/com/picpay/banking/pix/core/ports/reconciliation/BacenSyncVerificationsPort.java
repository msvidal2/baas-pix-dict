package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.Vsync.VsyncResult;

public interface BacenSyncVerificationsPort {

    VsyncResult syncVerification(String vsync);

}
