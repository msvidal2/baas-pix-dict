package com.picpay.banking.reconciliation.ports.bacen;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class BacenContentIdentifierEventsPortImpl implements BacenContentIdentifierEventsPort {


    @Override
    public List<ContentIdentifierEvent> list(final KeyType keyType, final LocalDateTime startTime, final LocalDateTime endTime) {
        return null;
    }

    @Override
    public ContentIdentifierFile requestContentIdentifierFile(final KeyType keyType) {
        return null;
    }

    @Override
    public Optional<ContentIdentifierFile> getContentIdentifierFileInBacen(final Integer id) {
        return Optional.empty();
    }

    @Override
    public List<String> downloadFile(final String url) {
        return null;
    }

}
