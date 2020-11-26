package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
public class FailureReconciliationSyncByFileUseCase {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final DatabaseContentIdentifierPort databaseContentIdentifierPort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final CreatePixKeyUseCase createPixKeyUseCase;
    private final RemovePixKeyUseCase removePixKeyUseCase;

    public void execute(KeyType keyType) {
        this.databaseContentIdentifierPort.findFileRequested(keyType).ifPresent(this::processFile);
    }

    private void processFile(final ContentIdentifierFile contentIdentifierFile) {
        final var fileAvailableInBacen = this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(contentIdentifierFile.getId());

        fileAvailableInBacen.ifPresent(availableFile -> {
            this.databaseContentIdentifierPort.save(availableFile);

            // final var url = availableFile.getUrl().replace("^.*\\/\\/[^\\/]+:?[0-9]?\\/", urlGateway );//CHANGE HOST TO GATEWAY

            final var cids = this.bacenContentIdentifierEventsPort.downloadFile(availableFile.getUrl());

            final var keyType = availableFile.getKeyType();

            this.verifyKeysOfOurOwnershipOutOfSync(cids, keyType);
            this.verifyKeysOfUnknowOwnershipOutOfSync(cids, keyType);
        });
    }

    private void verifyKeysOfOurOwnershipOutOfSync(List<String> cidsInBacen, KeyType keyType) {

        final var cidsToInsert = this.databaseContentIdentifierPort.findCidsNotSync(keyType, cidsInBacen);
        log.info("Verifying Keys in Bacen to insert in database - cidsToInsert size {}", cidsToInsert.size());

        cidsToInsert.stream()
            .forEach(cid -> {
                final var pixKey = this.bacenPixKeyByContentIdentifierPort.getPixKey(cid);
                this.createPixKeyUseCase.execute(UUID.randomUUID().toString(), pixKey, CreateReason.RECONCILIATION);
                log.info("Cid {} of key type {} inserted in database", cid, keyType);
            });
    }

    private void verifyKeysOfUnknowOwnershipOutOfSync(final List<String> cidsInBacen, final KeyType keyType) {

        final var cidsToRemove = this.databaseContentIdentifierPort.findCidsNotSyncToRemove(keyType, cidsInBacen);
        log.info("Verifying Keys in Bacen to remove from database - cidsToRemove size {}", cidsToRemove.size());
        cidsToRemove.forEach(cid -> {
            final var pixKey = this.databaseContentIdentifierPort.findPixKey(cid); //PEGAR DO NOSSO BANCO
            pixKey.ifPresent(key -> {
                this.removePixKeyUseCase.execute(UUID.randomUUID().toString(), key, RemoveReason.RECONCILIATION);
                log.info("Cid {} of key type {} is removed from database because don't exists in bacen", cid, keyType);
            });
        });
    }

}
