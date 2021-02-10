package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.domain.Sync;
import com.picpay.banking.pix.core.domain.SyncVerifier;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.picpay.banking.pix.core.domain.ContentIdentifierFileAction.ADDED;
import static com.picpay.banking.pix.core.domain.ContentIdentifierFileAction.REMOVED;
import static com.picpay.banking.pix.core.domain.ContentIdentifierFileAction.UPDATED;
import static net.logstash.logback.argument.StructuredArguments.kv;

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
    private final ReconciliationLockPort lockPort;
    private final SyncVerifierPort syncVerifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;

    public void execute(KeyType keyType) {
        this.databaseContentIdentifierPort.findLastFileRequested(keyType)
            .ifPresent(contentIdentifierFile -> {
                try {

                    if(contentIdentifierFile.isNotProcessed())
                        this.processFile(contentIdentifierFile);

                } catch (Exception e){
                    log.error("Error while executing sync by file" , e);
                } finally {
                    this.lockPort.unlock();
                }
            });
    }

    private void processFile(final ContentIdentifierFile contentIdentifierFile) {
        final var contentIdentifierFileId = contentIdentifierFile.getId();
        final var availableFile = this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(contentIdentifierFileId);

        if (availableFile == null || availableFile.getStatus().isNotAvaliable()) {
            return;
        }

        final var keyType = availableFile.getKeyType();

        log.info("ReconciliationSyncByFile_started {} {}", kv("contentIdentifierFileId", contentIdentifierFileId), kv("keyType",keyType));

        this.lockPort.lock();

        final var cids = this.bacenContentIdentifierEventsPort.downloadCidsFromBacen(availableFile.getUrl());

        var sync = this.verifyCidsInDatabase(contentIdentifierFile, cids, keyType);

        this.synchronizeCids(contentIdentifierFileId,keyType, sync);

        availableFile.done();

        this.databaseContentIdentifierPort.saveFile(availableFile);

        final var vsyncCurrent = this.findPixKeyPort.computeVsync(keyType);
        var syncVerifierResult = bacenSyncVerificationsPort.syncVerification(keyType, vsyncCurrent);
        var lstSyncVerifier = syncVerifierPort.getLastSuccessfulVsync(keyType).orElseGet(() -> SyncVerifier.builder()
            .keyType(keyType)
            .synchronizedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
            .build());
        var newSyncVerifierHistoric = lstSyncVerifier.syncVerificationResult(vsyncCurrent, syncVerifierResult);

        syncVerifierPort.save(lstSyncVerifier);
        syncVerifierHistoricPort.save(newSyncVerifierHistoric);

        log.info("ReconciliationSyncByFile_ended {} {}", kv("contentIdentifierFileId", contentIdentifierFileId),kv("keyType",keyType));
    }

    private Sync verifyCidsInDatabase(final ContentIdentifierFile contentIdentifierFile, final java.util.List<String> cids, final KeyType keyType) {
        final var sync = new Sync(contentIdentifierFile);

        var cidsInDatabase = this.extractCidsInDatabaseByPagination(keyType);

        sync.verify(cids, cidsInDatabase);

        return sync;
    }

    private List<String> extractCidsInDatabaseByPagination(final KeyType keyType) {
        Pagination<PixKey> pagination;
        var actualPage = 0;
        List<String> cidsInDatabase = new ArrayList<>();
        do {
            pagination = this.findPixKeyPort.findAllByKeyType(keyType, actualPage, 1000);
            final var cidsAlreadyInDatabase = pagination.getResult().stream().map(PixKey::getCid).collect(Collectors.toList());
            cidsInDatabase.addAll(cidsAlreadyInDatabase);
            actualPage = pagination.nextPage();
        } while (pagination.getHasNext());

        return cidsInDatabase;
    }

    private void synchronizeCids(final Integer contentIdentifierFileId,final KeyType keyType, final Sync sync) {
        sync.getCidsNotSyncronized().parallelStream()
            .forEach(cid -> {
                this.bacenPixKeyByContentIdentifierPort.getPixKey(cid)
                    .ifPresentOrElse(pixKey -> {
                        final var pixKeyToInsert = pixKey.toBuilder().cid(cid).build();
                        final var actualKey = this.findPixKeyPort.findPixKey(pixKeyToInsert.getKey());
                        this.createPixKeyPort.savePixKey(pixKeyToInsert, Reason.RECONCILIATION);

                        actualKey.ifPresentOrElse(keyInDatabase -> {
                            updatePixKey(contentIdentifierFileId, keyType, sync, cid, pixKeyToInsert);
                        }, () -> {
                            insertPixKey(contentIdentifierFileId, keyType, sync, cid, pixKeyToInsert);
                        });
                    }, () -> {
                        this.remove(contentIdentifierFileId, keyType, sync, cid);
                    });
            });
    }

    private void insertPixKey(final Integer contentIdentifierFileId,final KeyType keyType, final Sync sync, final String cid, final PixKey pixKeyToInsert) {
        this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKeyToInsert, cid, ADDED);
        this.pixKeyEventPort.pixKeyWasCreated(pixKeyToInsert);
        log.info("ReconciliationSyncByFile_changePixKey: {}, {}, {}",
            kv("key", pixKeyToInsert.getKey()),
            kv("keyType", keyType),
            kv("action", ADDED),
            kv("contentIdentifierFileId", contentIdentifierFileId));
    }

    private void updatePixKey(final Integer contentIdentifierFileId,final KeyType keyType, final Sync sync, final String cid, final PixKey pixKeyToInsert) {
        this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKeyToInsert, cid, UPDATED);
        this.pixKeyEventPort.pixKeyWasUpdated(pixKeyToInsert);
        log.info("ReconciliationSyncByFile_changePixKey: {}, {}, {}, {}",
            kv("key", pixKeyToInsert.getKey()),
            kv("keyType", keyType),
            kv("action", UPDATED),
            kv("contentIdentifierFileId", contentIdentifierFileId));
    }


    private void remove(final Integer contentIdentifierFileId, final KeyType keyType, final Sync sync, final String cid) {
        final var cidInDatabase = this.findPixKeyPort.findByCid(cid);
        cidInDatabase.ifPresent(pixKey -> {
            final var calculatedCid = pixKey.recalculateCid();
            final var valueInBacen = this.bacenPixKeyByContentIdentifierPort.getPixKey(calculatedCid);
            if (valueInBacen.isEmpty()) {
                this.removePixKey(contentIdentifierFileId,keyType, sync, cid, pixKey);
            }
        });
    }

    private void removePixKey(final Integer contentIdentifierFileId, final KeyType keyType, final Sync sync, final String cid, final com.picpay.banking.pix.core.domain.PixKey pixKey) {
        this.removePixKeyPort.remove(pixKey.getKey(), participant);
        this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKey, cid, REMOVED);
        this.pixKeyEventPort.pixKeyWasRemoved(pixKey);
        log.info("ReconciliationSyncByFile_changePixKey: {}, {}, {}, {}",
            kv("key", pixKey.getKey()),
            kv("keyType", keyType),
            kv("action", REMOVED),
            kv("contentIdentifierFileId", contentIdentifierFileId));
    }

}
