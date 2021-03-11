package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.ContentIdentifierFileAction;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationFixByCidFile;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.exception.ReconciliationException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.picpay.banking.pix.core.domain.ContentIdentifierFileAction.ADDED;
import static com.picpay.banking.pix.core.domain.ContentIdentifierFileAction.REMOVED;
import static com.picpay.banking.pix.core.domain.ContentIdentifierFileAction.UPDATED;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class FailureReconciliationSyncByFileUseCase {

    private static final String LOG_CONTENT_IDENTIFIER_FIELD_ID = "contentIdentifierFileId";

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
    private final ReconciliationLockPort reconciliationLockPort;

    private ReconciliationFixByCidFile reconciliationFixByCidFile;

    public SyncVerifierHistoric execute(KeyType keyType) {
        var startCurrentTimeMillis = System.currentTimeMillis();
        SyncVerifierHistoric syncVerifierHistoric;
        try {
            reconciliationLockPort.lock();

            ContentIdentifierFile contentIdentifierFile = requestAwaitContentIdentifierFile(keyType);
            var cidsInDatabase = extractCidsInDatabaseByPagination(keyType);

            reconciliationFixByCidFile = new ReconciliationFixByCidFile(keyType, contentIdentifierFile);

            var cidsThatMustBeCreated = reconciliationFixByCidFile.computeCidsThatMustBeCreated(cidsInDatabase);
            createPixKey1000by1000(cidsThatMustBeCreated);

            reconciliationFixByCidFile.computeCidsThatMustBeRemoved(cidsInDatabase).parallelStream()
                .forEach(this::remove);

            syncVerifierHistoric = performSyncVerifier();

            contentIdentifierFile.done();
            databaseContentIdentifierPort.saveFile(contentIdentifierFile);
        } finally {
            reconciliationLockPort.unlock();
        }

        final int MILLISECONDS_TO_SECONDS = 1000;
        log.info("ReconciliationSyncByFile_ended {} {} {} {}",
            kv(LOG_CONTENT_IDENTIFIER_FIELD_ID, reconciliationFixByCidFile.getContentIdentifierFile().getId()),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / MILLISECONDS_TO_SECONDS),
            kv("countCidInFile", reconciliationFixByCidFile.getContentIdentifierFile().getContent().size()),
            kv("keyType", keyType));

        return syncVerifierHistoric;
    }

    private ContentIdentifierFile requestAwaitContentIdentifierFile(final KeyType keyType) {
        var contentIdentifierFile = requestSyncFileUseCase.requestAwaitFile(keyType);
        if (contentIdentifierFile.getContent().isEmpty()) {
            log.error("ReconciliationSyncByFile_error: The CID file is empty. {}, {}",
                kv(LOG_CONTENT_IDENTIFIER_FIELD_ID, contentIdentifierFile.getId()),
                kv("contentIdentifierFileUrl", contentIdentifierFile.getUrl()));
            throw new ReconciliationException("The CID file is empty.");
        }
        return contentIdentifierFile;
    }

    private void createPixKey1000by1000(final List<String> cidsThatMustBeCreated) {
        int offset = 0;
        final int INCREMENT = 1000;
        int total = cidsThatMustBeCreated.size();
        while (offset < total) {
            log.info("Processing {} of {}", offset, total);

            cidsThatMustBeCreated.stream().skip(offset).limit(INCREMENT)
                .flatMap(cid -> bacenPixKeyByContentIdentifierPort.getPixKey(cid).stream())
                .parallel()
                .forEach(pixKeyInBacen -> findPixKeyPort.findPixKey(pixKeyInBacen.getKey())
                    .ifPresentOrElse(pixKeyInDatabase -> update(pixKeyInBacen, pixKeyInDatabase),
                        () -> insert(pixKeyInBacen)));

            offset += INCREMENT;
        }
    }

    private SyncVerifierHistoric performSyncVerifier() {
        var keyType = reconciliationFixByCidFile.getKeyType();
        final var vsyncCurrent = findPixKeyPort.computeVsync(keyType);
        var syncVerifierResult = bacenSyncVerificationsPort.syncVerification(keyType, vsyncCurrent);
        var lastSyncVerifier = syncVerifierPort.getLastSuccessfulVsync(keyType);
        var newSyncVerifierHistoric = lastSyncVerifier.syncVerificationResult(vsyncCurrent, syncVerifierResult);

        syncVerifierPort.save(lastSyncVerifier);
        return syncVerifierHistoricPort.save(newSyncVerifierHistoric);
    }

    private List<String> extractCidsInDatabaseByPagination(KeyType keyType) {
        Pagination<PixKey> pagination;
        var actualPage = 0;
        final int SIZE = 1000;
        List<String> cidsInDatabase = new ArrayList<>();
        do {
            pagination = findPixKeyPort.findAllByKeyType(keyType, actualPage, SIZE);
            final var cidsAlreadyInDatabase = pagination.getResult().stream().map(PixKey::getCid).collect(Collectors.toList());
            cidsInDatabase.addAll(cidsAlreadyInDatabase);
            actualPage = pagination.nextPage();
        } while (BooleanUtils.isTrue(pagination.getHasNext()));

        return cidsInDatabase;
    }

    private void insert(final PixKey pixKeyInBacen) {
        var pixKey = createPixKeyPort.savePixKey(pixKeyInBacen, Reason.RECONCILIATION);
        pixKeyEventPort.pixKeyWasCreated(pixKey);
        createHistoricAction(pixKey, ADDED);
    }

    private void update(final PixKey pixKeyInBacen, final PixKey pixKeyInDatabase) {
        var pixKey = createPixKeyPort.savePixKey(pixKeyInBacen, Reason.RECONCILIATION);
        pixKeyEventPort.pixKeyWasUpdated(pixKey);

        createHistoricAction(pixKeyInDatabase, REMOVED);
        createHistoricAction(pixKeyInBacen, UPDATED);
    }

    private void remove(final String cid) {
        var pixKeyInDatabase = findPixKeyPort.findByCid(cid);
        pixKeyInDatabase.ifPresent(pixKey -> {
            removePixKeyPort.removeByCid(pixKey.getCid());
            pixKeyEventPort.pixKeyWasRemoved(pixKey);
            createHistoricAction(pixKey, ContentIdentifierFileAction.REMOVED);
        });
    }

    private void createHistoricAction(final PixKey pixKey, final ContentIdentifierFileAction action) {
        databaseContentIdentifierPort.saveAction(reconciliationFixByCidFile.getContentIdentifierFile().getId(), pixKey, pixKey.getCid(), action);

        log.info("ReconciliationSyncByFile_changePixKey: {}, {}, {}, {}",
            kv("key", pixKey.getKey()),
            kv("keyType", pixKey.getType()),
            kv("action", action),
            kv(LOG_CONTENT_IDENTIFIER_FIELD_ID, reconciliationFixByCidFile.getContentIdentifierFile().getId()));
    }

}
