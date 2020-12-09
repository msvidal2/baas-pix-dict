package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;

import java.time.LocalDateTime;
import java.util.List;

public interface BacenContentIdentifierEventsPort {

    List<ContentIdentifierEvent> list(KeyType keyType, LocalDateTime startTime, LocalDateTime endTime);

    ContentIdentifierFile requestContentIdentifierFile(KeyType keyType);

    ContentIdentifierFile getContentIdentifierFileInBacen(Integer id);

    List<String> downloadCidsFromBacen(String url);

}
