package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Vsync.VsyncResult;

import java.time.LocalDateTime;
import java.util.List;

public interface BacenReconciliationPort {

    VsyncResult syncVerification(String vsync);

    List<ContentIdentifier> list(KeyType keyType, LocalDateTime startTime, LocalDateTime endTime);

    PixKey getPixKey(String cid);

}
