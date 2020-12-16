package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.ReconciliationEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BacenContentIdentifierEventsPort {

    Set<ReconciliationEvent> list(KeyType keyType, LocalDateTime startTime, LocalDateTime endTime);

    ContentIdentifierFile requestContentIdentifierFile(KeyType keyType);

    ContentIdentifierFile getContentIdentifierFileInBacen(Integer id);

    List<String> downloadCidsFromBacen(String url);

}
