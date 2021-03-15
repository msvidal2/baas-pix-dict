package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.KeyType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ContentIdentifierPort {

    List<String> findAllCidsAfterLastSuccessfulVsync(KeyType keyType, LocalDateTime synchronizedAt);

    Set<BacenCidEvent> findBacenEventsAfter(KeyType keyType, LocalDateTime synchronizedStart);

}
