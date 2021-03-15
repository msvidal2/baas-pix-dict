package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;

import java.time.LocalDateTime;
import java.util.Set;

public interface BacenContentIdentifierEventsPort {

    Set<BacenCidEvent> findAllContentIdentifierEventsAfter(KeyType keyType, LocalDateTime startTime);

    ContentIdentifierFile requestContentIdentifierFile(KeyType keyType);

    ContentIdentifierFile getContentIdentifierFileInBacen(Integer id);

    Set<String> downloadContentIdentifierFromBacen(String url);

}
