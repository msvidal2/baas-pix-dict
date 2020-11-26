package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BacenContentIdentifierEventsPort {

    List<ContentIdentifierEvent> list(KeyType keyType, LocalDateTime startTime, LocalDateTime endTime);

    Optional<ContentIdentifierFile> requestContentIdentifierFile(KeyType keyType);

    Optional<ContentIdentifierFile> getContentIdentifierFileInBacen(Integer id);

    List<String> downloadFile(String url);

}
