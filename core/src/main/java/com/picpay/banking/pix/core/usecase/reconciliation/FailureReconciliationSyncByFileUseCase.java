package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

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
    private final SavePixKeyPort createPixKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;
    private final PixKeyEventPort pixKeyEventPort;

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

        var sync = this.verifyCidsInDatabase(contentIdentifierFile, cids, keyType);

        this.synchronizeCids(keyType, sync);

        this.databaseContentIdentifierPort.saveFile(availableFile);
    }

    private Sync verifyCidsInDatabase(final ContentIdentifierFile contentIdentifierFile, final java.util.List<String> cids, final KeyType keyType) {
        final var sync = new Sync(contentIdentifierFile);

        var actualPage = 0;
        Pagination<PixKey> pagination = null;

        do{
            pagination = this.findPixKeyPort.findAllByKeyType(keyType,actualPage,10);
            sync.verify(cids, pagination.getResult());
            actualPage = pagination.nextPage();
        } while (pagination.getHasNext());

        return sync;
    }

    private void synchronizeCids(final KeyType keyType, final Sync sync) {
        sync.getCidsNotSyncronized().stream()
            .forEach(cid -> {
                this.bacenPixKeyByContentIdentifierPort.getPixKey(cid).ifPresentOrElse(pixKey -> {
                    final var pixKeyToInsert = pixKey.toBuilder().cid(cid).build();

                    final var actualKey = this.findPixKeyPort.findPixKey(pixKeyToInsert.getKey());
                    this.createPixKeyPort.savePixKey(pixKeyToInsert, Reason.RECONCILIATION);

                    actualKey.ifPresentOrElse(keyInDatabase -> {
                        this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKeyToInsert, cid, UPDATED);
                        this.pixKeyEventPort.pixKeyWasEdited(keyInDatabase, pixKeyToInsert);
                        log.info("Cid {} of key type {} {} in database", cid, keyType,UPDATED);
                    }, () -> {
                        this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKeyToInsert, cid, ADDED);
                        this.pixKeyEventPort.pixKeyWasCreated(pixKeyToInsert);
                        log.info("Cid {} of key type {} {} in database", cid, keyType,ADDED);
                    } );


                }, () -> {
                    this.remove(keyType, sync, cid);
                });
            });
    }


    private void remove(final KeyType keyType, final Sync sync, final String cid) {
        final var cidInDatabase = this.findPixKeyPort.findByCid(cid);
        cidInDatabase.ifPresent(pixKey -> {
            final var calculatedCid = pixKey.recalculateCid();;
            final var valueInBacen = this.bacenPixKeyByContentIdentifierPort.getPixKey(calculatedCid);
            if (valueInBacen.isEmpty()) {
                this.removePixKey(keyType, sync, cid, pixKey);
            }else {
                log.info("PixKey {} - Type {} with Cid {} was update from database because cid don't exists in bacen but key exists", pixKey.getKey(),pixKey.getType(), cid);
            }
        });
    }

    private void removePixKey(final KeyType keyType, final Sync sync, final String cid, final com.picpay.banking.pix.core.domain.PixKey pixKey) {
        this.removePixKeyPort.remove(pixKey.getKey(), participant);
        this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKey, cid, REMOVED);
        this.pixKeyEventPort.pixKeyWasDeleted(pixKey, LocalDateTime.now());
        log.info("PixKey {} type {} with Cid {} of was removed from database because don't exists in bacen"
            , pixKey.getCid(), pixKey.getKey(), keyType);
    }

}
