package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Sync;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.picpay.banking.pix.core.domain.ContentIdentifierFileAction.ADDED;
import static com.picpay.banking.pix.core.domain.ContentIdentifierFileAction.REMOVED;
import static com.picpay.banking.pix.core.domain.ContentIdentifierFileAction.UPDATED;

@AllArgsConstructor
@Slf4j
public class FailureReconciliationSyncByFileUseCase {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final DatabaseContentIdentifierPort databaseContentIdentifierPort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final CreatePixKeyPort createPixKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;
    private Integer participant;

    public void execute(KeyType keyType) {
        this.databaseContentIdentifierPort.findLastFileRequested(keyType).ifPresent(this::processFile);
    }

    private void processFile(final ContentIdentifierFile contentIdentifierFile) {
        final var availableFile = this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(contentIdentifierFile.getId());

        if (availableFile == null || availableFile.getStatus().isNotAvaliable()) {
            return;
        }

        final var cids = this.bacenContentIdentifierEventsPort.downloadFile(availableFile.getUrl());
        final var keyType = availableFile.getKeyType();

        final var contentIdentifiers = this.databaseContentIdentifierPort.listAll(keyType);

        final var sync = new Sync(contentIdentifierFile);
        sync.verify(cids, contentIdentifiers);

        this.synchronizeCids(keyType, sync);

        this.databaseContentIdentifierPort.saveFile(availableFile);
    }

    private void synchronizeCids(final KeyType keyType, final Sync sync) {
        sync.getCidsNotSyncronized().stream()
            .forEach(cid -> {
                this.bacenPixKeyByContentIdentifierPort.getPixKey(cid).ifPresentOrElse(pixKey -> {
                    this.createPixKeyPort.createPixKey(pixKey, CreateReason.RECONCILIATION);

                    this.insertCID(keyType, cid, pixKey);

                    this.insertAuditLog(keyType, sync, cid, pixKey);
                }, () -> {
                    this.remove(keyType, sync, cid);
                });
            });
    }

    private void insertCID(final KeyType keyType, final String cid, final com.picpay.banking.pix.core.domain.PixKey pixKeyInBacen) {
        final var contentIdentifier = ContentIdentifier.builder()
            .key(pixKeyInBacen.getKey())
            .keyType(keyType)
            .cid(cid)
            .build();
        this.databaseContentIdentifierPort.save(contentIdentifier);
    }

    private void insertAuditLog(final KeyType keyType, final Sync sync, final String cid,
        final com.picpay.banking.pix.core.domain.PixKey pixKeyInBacen) {
        final var keyInDatabase = this.findPixKeyPort.findPixKey(pixKeyInBacen.getKey());
        keyInDatabase.ifPresentOrElse(pixKey -> {
            this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKeyInBacen, cid, UPDATED);
            log.info("Cid {} of key type {} updated in database", cid, keyType);
        }, () -> {
            this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKeyInBacen, cid, ADDED);
            log.info("Cid {} of key type {} was inserted in database", cid, keyType);
        });
    }

    private void remove(final KeyType keyType, final Sync sync, final String cid) {
        final var cidInDatabase = this.databaseContentIdentifierPort.findByCid(cid);
        cidInDatabase.ifPresent(contentIdentifier -> {
            this.databaseContentIdentifierPort.delete(cid);
            contentIdentifier.getPixKey().calculateCid(); // TODO: Melhorar este ponto
            final var calculatedCid = contentIdentifier.getPixKey().getCid();
            final var valueInBacen = this.bacenPixKeyByContentIdentifierPort.getPixKey(calculatedCid);
            if (!valueInBacen.isPresent()) {
                this.removePixKeyPort.remove(contentIdentifier.getKey(), participant);
                this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), contentIdentifier.getPixKey(), cid, REMOVED);
                log.info("Cid {} of pixKey {} type {} was removed from database because don't exists in bacen"
                    , contentIdentifier.getCid(), contentIdentifier.getKey(), keyType);
                return;
            }
            log.info("Only Cid {} was removed from database because cid don't exists in bacen but key exists", cid);
        });
        return;
    }

}
