package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.KeyType;

import java.time.LocalDateTime;
import java.util.Set;

public interface ContentIdentifierPort {

    Set<String> findAllCidsAfterLastSuccessfulVsync(KeyType keyType, LocalDateTime synchronizedAt);

}
