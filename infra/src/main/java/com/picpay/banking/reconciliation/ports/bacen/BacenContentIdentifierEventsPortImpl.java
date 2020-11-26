package com.picpay.banking.reconciliation.ports.bacen;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.reconciliation.clients.BacenArqClient;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class BacenContentIdentifierEventsPortImpl implements BacenContentIdentifierEventsPort {

    private BacenArqClient bacenArqClient;

    private BacenReconciliationClient bacenReconciliationClient;

    private String participant;

    public BacenContentIdentifierEventsPortImpl(final BacenArqClient bacenArqClient, final BacenReconciliationClient bacenReconciliationClient, @Value("${picpay.ispb}") String participant) {
        this.bacenArqClient = bacenArqClient;
        this.bacenReconciliationClient = bacenReconciliationClient;
        this.participant = participant;
    }

    @Override
    public List<ContentIdentifierEvent> list(final KeyType keyType, final LocalDateTime startTime, final LocalDateTime endTime) {
        return null;
    }

    @Override
    public ContentIdentifierFile requestContentIdentifierFile(final KeyType keyType) {

        //return this.bacenSyncClient.requestCid(new CidSetFileRequest());
        return null;
    }

    @Override
    public Optional<ContentIdentifierFile> getContentIdentifierFileInBacen(final Integer id) {
        return Optional.empty();
    }

    @Override
    public List<String> downloadFile(final String url) {
        final var file = this.bacenArqClient.request(URI.create(url));
        return Arrays.asList(file.split("\n"));
    }

}
