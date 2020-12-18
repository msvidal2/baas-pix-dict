package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
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

    private final Integer participant;
    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final DatabaseContentIdentifierPort databaseContentIdentifierPort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final CreatePixKeyPort createPixKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;

    public void execute(KeyType keyType) {
        this.databaseContentIdentifierPort.findLastFileRequested(keyType).ifPresent(this::processFile);
    }

    private void processFile(final ContentIdentifierFile contentIdentifierFile) {
        final var availableFile = this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(contentIdentifierFile.getId());

        if (availableFile == null || availableFile.getStatus().isNotAvaliable()) {
            return;
        }

        final var cids = this.bacenContentIdentifierEventsPort.downloadCidsFromBacen(availableFile.getUrl());
        final var keyType = availableFile.getKeyType();

        final var contentIdentifiers = this.findPixKeyPort.findAllByKeyType(keyType);

        final var sync = new Sync(contentIdentifierFile);
        sync.verify(cids, contentIdentifiers);

        this.synchronizeCids(keyType, sync);

        this.databaseContentIdentifierPort.saveFile(availableFile);
    }

    private void synchronizeCids(final KeyType keyType, final Sync sync) {
        sync.getCidsNotSyncronized().stream()
            .forEach(cid -> {
                this.bacenPixKeyByContentIdentifierPort.getPixKey(cid).ifPresentOrElse(pixKey -> {
                    final var pixKeyToInsert = pixKey.toBuilder().cid(cid).build();
                    this.createPixKeyPort.createPixKey(pixKeyToInsert, CreateReason.RECONCILIATION);

                    this.insertAuditLog(keyType, sync, cid, pixKeyToInsert);
                }, () -> {
                    this.remove(keyType, sync, cid);
                });
            });
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
        final var cidInDatabase = this.findPixKeyPort.findByCid(cid);
        cidInDatabase.ifPresent(pixKey -> {
            final var calculatedCid = pixKey.recalculateCid();;
            final var valueInBacen = this.bacenPixKeyByContentIdentifierPort.getPixKey(calculatedCid);
            if (valueInBacen.isEmpty()) {
                this.removePixKey(keyType, sync, cid, pixKey);
            }
            log.info("Only Cid {} was removed from database because cid don't exists in bacen but key exists", cid);
        });
    }

    private void removePixKey(final KeyType keyType, final Sync sync, final String cid, final com.picpay.banking.pix.core.domain.PixKey pixKey) {
        this.removePixKeyPort.remove(pixKey.getKey(), participant);
        this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKey, cid, REMOVED);
        log.info("Cid {} of pixKey {} type {} was removed from database because don't exists in bacen"
            , pixKey.getCid(), pixKey.getKey(), keyType);
    }

}
