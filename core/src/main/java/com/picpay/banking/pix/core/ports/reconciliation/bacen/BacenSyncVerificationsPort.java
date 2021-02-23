package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierResult;

public interface BacenSyncVerificationsPort {

    SyncVerifierResult syncVerification(KeyType keyType, String vsync);

}
