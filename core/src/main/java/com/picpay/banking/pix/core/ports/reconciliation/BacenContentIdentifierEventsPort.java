package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierAction;
import com.picpay.banking.pix.core.domain.KeyType;

import java.time.LocalDateTime;
import java.util.List;

public interface BacenContentIdentifierEventsPort {

    List<ContentIdentifierAction> list(KeyType keyType, LocalDateTime startTime, LocalDateTime endTime);

}
