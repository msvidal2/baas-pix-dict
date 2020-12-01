package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.SyncVerifierResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReconciliationBacenPort {

    SyncVerifierResult syncVerification(String vsync);

    List<ContentIdentifierEvent> list(KeyType keyType, LocalDateTime startTime, LocalDateTime endTime);

    Optional<PixKey> getPixKey(String cid);

}
