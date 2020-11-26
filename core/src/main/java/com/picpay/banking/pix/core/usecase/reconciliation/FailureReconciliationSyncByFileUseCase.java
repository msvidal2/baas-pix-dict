package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class FailureReconciliationSyncByFileUseCase {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final DatabaseContentIdentifierPort databaseContentIdentifierPort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final FindPixKeyPort findPixKeyPort;
    private final CreatePixKeyPort createPixKeyPort;

    public void execute(KeyType keyType) {
        this.databaseContentIdentifierPort.findLastFileRequested(keyType).ifPresent(this::processFile);
    }

    private void processFile(final ContentIdentifierFile contentIdentifierFile) {
        final var availableFile = this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(contentIdentifierFile.getId());

        if(availableFile == null || availableFile.getStatus().isNotAvaliable()) {
            return;
        }

        this.databaseContentIdentifierPort.save(availableFile);
        final var cids = this.bacenContentIdentifierEventsPort.downloadFile(availableFile.getUrl());
        final var keyType = availableFile.getKeyType();


    }



    private void verifyKeysOfOurOwnershipOutOfSync(List<String> cidsInBacen, KeyType keyType) {

        final var cidsToInsert = this.databaseContentIdentifierPort.findCidsNotSync(keyType, cidsInBacen);
        log.info("Verifying Keys in Bacen to insert in database - cidsToInsert size {}", cidsToInsert.size());

        //TODO
        cidsToInsert.stream()
            .forEach(cid -> {
                final var pixKey = this.bacenPixKeyByContentIdentifierPort.getPixKey(cid);
                this.createPixKeyPort.createPixKey(pixKey, CreateReason.RECONCILIATION);
                log.info("Cid {} of key type {} inserted in database", cid, keyType);
            });
    }

    private void verifyKeysOfUnknowOwnershipOutOfSync(final List<String> cidsInBacen, final KeyType keyType) {

        final var keysToRemove = this.databaseContentIdentifierPort.findKeysNotSyncToRemove(keyType, cidsInBacen);
        log.info("Verifying Keys in Bacen to remove from database - keysToRemove size {}", keysToRemove.size());
        keysToRemove.forEach(keyToRemove -> {
             this.findPixKeyPort.findPixKey(keyToRemove)
                .ifPresent(pixKey -> {
                    //this.removePixKeyPort.remove(UUID.randomUUID().toString(), pixKey, RemoveReason.RECONCILIATION);
                    log.info("Cid {} of pixKey {} type {} is removed from database because don't exists in bacen", keyToRemove,pixKey.getKey(), keyType);
                });
        });
    }

}
