package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class RequestSyncFileUseCase {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final DatabaseContentIdentifierPort databaseContentIdentifierPort;

    public ContentIdentifierFile execute(KeyType keyType){
        log.info("RequestSyncFile_started {}", kv("keyType", keyType));
        final var contentIdentifierFile = this.bacenContentIdentifierEventsPort.requestContentIdentifierFile(keyType);
        this.databaseContentIdentifierPort.saveFile(contentIdentifierFile);
        log.info("RequestSyncFile_ended {} {} ", kv("keyType", keyType), kv("contentIdentifierFileId",contentIdentifierFile.getId()));
        return contentIdentifierFile;
    }

}
