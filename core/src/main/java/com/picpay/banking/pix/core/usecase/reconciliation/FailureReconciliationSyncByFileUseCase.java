package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.common.Pagination;
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
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

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
    private final DatabaseContentIdentifierPort databaseContentIdentifierPort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final SavePixKeyPort createPixKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;
    private final PixKeyEventPort pixKeyEventPort;
    private final SyncVerifierPort syncVerifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final RequestSyncFileUseCase requestSyncFileUseCase;

    public void execute(KeyType keyType) {
        var resultCidFile = requestSyncFileUseCase.requestAwaitFile(keyType);

        if (!CollectionUtils.isEmpty(resultCidFile.getContent()) && resultCidFile.isNotProcessed()) {
            try {
                processFile(resultCidFile);
            } catch (Exception e) {
                log.error("Error while executing sync by file", e);
            }
        }
    }

    private void processFile(final ContentIdentifierFile contentIdentifierFile) {
        var sync = this.verifyCidsInDatabase(contentIdentifierFile, contentIdentifierFile.getContent(), contentIdentifierFile.getKeyType());
        synchronizeCids(contentIdentifierFile.getId(), contentIdentifierFile.getKeyType(), sync);
        contentIdentifierFile.done();

        this.databaseContentIdentifierPort.saveFile(contentIdentifierFile);

        final var vsyncCurrent = this.findPixKeyPort.computeVsync(contentIdentifierFile.getKeyType());
        var syncVerifierResult = bacenSyncVerificationsPort.syncVerification(contentIdentifierFile.getKeyType(), vsyncCurrent);
        var lstSyncVerifier = syncVerifierPort.getLastSuccessfulVsync(contentIdentifierFile.getKeyType()).orElseGet(() -> SyncVerifier.builder()
            .keyType(contentIdentifierFile.getKeyType())
            .synchronizedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
            .build());
        var newSyncVerifierHistoric = lstSyncVerifier.syncVerificationResult(vsyncCurrent, syncVerifierResult);

        syncVerifierPort.save(lstSyncVerifier);
        syncVerifierHistoricPort.save(newSyncVerifierHistoric);

        log.info("ReconciliationSyncByFile_ended {} {}", kv("contentIdentifierFileId", contentIdentifierFile.getId()), kv("keyType",
                                                                                                                          contentIdentifierFile.getKeyType()));
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
        } while (BooleanUtils.isTrue(pagination.getHasNext()));

        return cidsInDatabase;
    }

    private void synchronizeCids(final Integer contentIdentifierFileId, final KeyType keyType, final Sync sync) {
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

    private void insertPixKey(final Integer contentIdentifierFileId, final KeyType keyType, final Sync sync, final String cid,
                              final PixKey pixKeyToInsert) {
        this.databaseContentIdentifierPort.saveAction(sync.getContentIdentifierFile().getId(), pixKeyToInsert, cid, ADDED);
        this.pixKeyEventPort.pixKeyWasCreated(pixKeyToInsert);
        log.info("ReconciliationSyncByFile_changePixKey: {}, {}, {}",
                 kv("key", pixKeyToInsert.getKey()),
                 kv("keyType", keyType),
                 kv("action", ADDED),
                 kv("contentIdentifierFileId", contentIdentifierFileId));
    }

    private void updatePixKey(final Integer contentIdentifierFileId, final KeyType keyType, final Sync sync, final String cid,
                              final PixKey pixKeyToInsert) {
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
                this.removePixKey(contentIdentifierFileId, keyType, sync, cid, pixKey);
            }
        });
    }

    private void removePixKey(final Integer contentIdentifierFileId, final KeyType keyType, final Sync sync, final String cid,
                              final com.picpay.banking.pix.core.domain.PixKey pixKey) {
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
