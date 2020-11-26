package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class RequestSyncFileUseCase {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final DatabaseContentIdentifierPort databaseContentIdentifierPort;

    public void execute(KeyType keyType){

        log.info("Requesting CID files from BACEN for key type", keyType);

        final var contentIdentifierFile = this.bacenContentIdentifierEventsPort.requestContentIdentifierFile(keyType);

        contentIdentifierFile.ifPresent(bacenContentIdentifierFile -> {
            this.databaseContentIdentifierPort.save(bacenContentIdentifierFile);
            log.info("CID files from BACEN requested succesfull {}", contentIdentifierFile);
        });


    }

}
