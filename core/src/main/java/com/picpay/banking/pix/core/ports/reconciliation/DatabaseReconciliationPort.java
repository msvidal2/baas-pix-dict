package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Vsync;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DatabaseReconciliationPort {

    Optional<Vsync> getLastSuccessfulVsync(KeyType keyType);

    void updateVerificationResult(Vsync vsync);

    List<ContentIdentifier> listAfterLastSuccessfulVsync(KeyType keyType, LocalDateTime synchronizedAt);

}
