package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationFixByCidFile;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoricAction;
import com.picpay.banking.pix.core.exception.ReconciliationException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierFilePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.validators.reconciliation.VsyncHistoricValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class FileReconciliationCheckUseCase {

    private static final String LOG_CONTENT_IDENTIFIER_FIELD_ID = "contentIdentifierFileId";
    private static final String SYNC_VERIFIER_HISTORIC = "syncVerifierHistoric";

    private final ContentIdentifierFilePort contentIdentifierFilePort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final FindPixKeyPort findPixKeyPort;
    private final SyncVerifierPort syncVerifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final RequestSyncFileUseCase requestSyncFileUseCase;
    private final ReconciliationLockPort reconciliationLockPort;
    private final ReconciliationActionPort reconciliationActionPort;

    private ReconciliationFixByCidFile reconciliationFixByCidFile;
    private SyncVerifierHistoric failureSyncVerifier;

    public SyncVerifierHistoric execute(SyncVerifierHistoric failureSyncVerifier) {
        var startCurrentTimeMillis = System.currentTimeMillis();

        VsyncHistoricValidator.validate(failureSyncVerifier);
        this.failureSyncVerifier = failureSyncVerifier;

        SyncVerifierHistoric syncVerifierHistoric;
        try {
            reconciliationLockPort.lock();
            syncVerifierHistoric = run(failureSyncVerifier.getKeyType());
        } finally {
            reconciliationLockPort.unlock();
        }

        final int MILLISECONDS_TO_SECONDS = 1000;
        log.info("ReconciliationSyncByFile_ended {} {} {} {}",
            kv(LOG_CONTENT_IDENTIFIER_FIELD_ID, reconciliationFixByCidFile.getContentIdentifierFile().getId()),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / MILLISECONDS_TO_SECONDS),
            kv("countCidInFile", reconciliationFixByCidFile.getContentIdentifierFile().getContent().size()),
            kv("keyType", failureSyncVerifier.getKeyType()));

        return syncVerifierHistoric;
    }

    private SyncVerifierHistoric run(final KeyType keyType) {
        ContentIdentifierFile contentIdentifierFile = requestAwaitContentIdentifierFile(keyType);
        var cidsInDatabase = extractCidsInDatabaseByPagination(keyType);

        reconciliationFixByCidFile = new ReconciliationFixByCidFile(keyType, contentIdentifierFile);

        var cidsThatMustBeCreated = reconciliationFixByCidFile.computeCidsThatMustBeCreated(cidsInDatabase);
        createPixKey1000by1000(cidsThatMustBeCreated);

        reconciliationFixByCidFile.computeCidsThatMustBeRemoved(cidsInDatabase).parallelStream()
            .forEach(cid -> findPixKeyPort.findByCid(cid).ifPresent(this::removePixKey));

        failureSyncVerifier.fixedByFileReconciliation();
        syncVerifierHistoricPort.save(failureSyncVerifier);

        var syncVerifierHistoric = performSyncVerifier();

        contentIdentifierFile.done();
        contentIdentifierFilePort.saveFile(contentIdentifierFile);
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
                .flatMap(cid -> bacenPixKeyByContentIdentifierPort.getPixKeyByCid(cid).stream())
                .parallel()
                .forEach(pixKeyInBacen -> findPixKeyPort.findPixKey(pixKeyInBacen.getKey())
                    .ifPresentOrElse(pixKeyInDatabase -> updatePixKey(pixKeyInDatabase, pixKeyInBacen),
                        () -> insertPixKey(pixKeyInBacen)));

            offset += INCREMENT;
        }
    }

    private void insertPixKey(final PixKey pixKey) {
        reconciliationActionPort.insertPixKey(createHistoricAction(pixKey, ReconciliationAction.ADDED));
    }

    private void updatePixKey(final PixKey oldPixKey, final PixKey newPixKey) {
        reconciliationActionPort.updatePixKey(createHistoricAction(oldPixKey, ReconciliationAction.REMOVED),
            createHistoricAction(newPixKey, ReconciliationAction.ADDED));
    }

    private void removePixKey(final PixKey pixKey) {
        reconciliationActionPort.removePixKey(createHistoricAction(pixKey, ReconciliationAction.REMOVED));
    }

    private SyncVerifierHistoricAction createHistoricAction(final PixKey pixKey, final ReconciliationAction action) {
        var historicAction = SyncVerifierHistoricAction.builder()
            .cid(pixKey.getCid())
            .pixKey(pixKey)
            .action(action)
            .syncVerifierHistoric(failureSyncVerifier)
            .build();

        log.info("FailureReconciliationSync_changePixKey: {}, {}, {}",
            kv("cid", pixKey.getCid()),
            kv("action", action),
            kv(SYNC_VERIFIER_HISTORIC, failureSyncVerifier));

        return historicAction;
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

}
