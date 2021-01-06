package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BacenContentIdentifierEventsPort {

    Set<BacenCidEvent> list(KeyType keyType, LocalDateTime startTime);

    ContentIdentifierFile requestContentIdentifierFile(KeyType keyType);

    ContentIdentifierFile getContentIdentifierFileInBacen(Integer id);

    List<String> downloadCidsFromBacen(String url);

    Optional<PixKey> getPixKey(String cid);

}
