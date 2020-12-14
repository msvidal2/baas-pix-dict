package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.ReconciliationsException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.pix.core.validators.reconciliation.VsyncHistoricValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.function.Consumer;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class FailureReconciliationSyncUseCase {

    private static final String LOG_START_TIME = "startCurrentTimeMillis";
    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final SyncVerifierHistoricActionPort syncVerifierHistoricActionPort;
    private final ContentIdentifierEventPort contentIdentifierEventPort;
    private final CreatePixKeyPort createPixKeyPort;
    private final UpdateAccountPixKeyPort updateAccountPixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private long startCurrentTimeMillis;

    public void execute(SyncVerifierHistoric syncVerifierHistoric) {
        this.startCurrentTimeMillis = System.currentTimeMillis();
        log.info("FailureReconciliationSync_started {} {}",
            kv(LOG_START_TIME, startCurrentTimeMillis),
            kv("vsyncHistoric", syncVerifierHistoric));

        VsyncHistoricValidator.validate(syncVerifierHistoric);

        Set<ContentIdentifierEvent> bacenEvents = bacenContentIdentifierEventsPort
            .list(syncVerifierHistoric.getKeyType(), syncVerifierHistoric.getSynchronizedStart(), LocalDateTime.now());

        Set<ContentIdentifierEvent> databaseEvents = contentIdentifierEventPort
            .findAllAfterLastSuccessfulVsync(syncVerifierHistoric.getKeyType(), syncVerifierHistoric.getSynchronizedStart());

        var contentIdentifierActions = syncVerifierHistoric.identifyActions(bacenEvents, databaseEvents);
        if (contentIdentifierActions.isEmpty()) {
            throw new ReconciliationsException(
                String.format("No action found for %s type reconciliation", syncVerifierHistoric.getKeyType().name()));
        }

        contentIdentifierActions.stream().filter(contentIdentifierAction -> contentIdentifierAction.getActionClassification().equals(
            SyncVerifierHistoricAction.ActionClassification.HAS_IN_BACEN_AND_NOT_HAVE_IN_DATABASE))
            .forEach(this::hasInBacenAndNotHaveInDatabase);

        contentIdentifierActions.stream().filter(contentIdentifierAction -> contentIdentifierAction.getActionClassification().equals(
            SyncVerifierHistoricAction.ActionClassification.HAS_IN_DATABASE_AND_NOT_HAVE_IN_BACEN))
            .forEach(this::hasInDatabaseAndNotHaveInBacen);

        syncVerifierHistoricActionPort.saveAll(contentIdentifierActions);

        log.info("FailureReconciliationSync_ended {} {} {}",
            kv(LOG_START_TIME, startCurrentTimeMillis),
            kv("contentIdentifierActions", contentIdentifierActions.size()),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / 1000));
    }

    private void hasInBacenAndNotHaveInDatabase(SyncVerifierHistoricAction action) {
        Consumer<PixKey> pixKeyPresentInTheBacen = pixKeyInBacen ->
            findPixKeyPort.findPixKey(pixKeyInBacen.getKey()).ifPresentOrElse(pixKeyInDataBase ->
                update(pixKeyInBacen, pixKeyInDataBase, true), () -> save(pixKeyInBacen, true));

        Runnable pixKeyNotPresentInTheBacen = () ->
            contentIdentifierEventPort.findPixKeyByContentIdentifier(action.getCid())
                .ifPresent(pixKeyInDataBase -> delete(pixKeyInDataBase, true));

        bacenPixKeyByContentIdentifierPort.getPixKey(action.getCid()).ifPresentOrElse(pixKeyPresentInTheBacen, pixKeyNotPresentInTheBacen);
    }

    private void hasInDatabaseAndNotHaveInBacen(final SyncVerifierHistoricAction action) {
        Consumer<PixKey> pixKeyPresentInTheBacen = pixKeyInBacen ->
            findPixKeyPort.findPixKey(pixKeyInBacen.getKey()).ifPresentOrElse(pixKeyInDataBase ->
                update(pixKeyInBacen, pixKeyInDataBase, false), () -> save(pixKeyInBacen, false));

        Runnable pixKeyNotPresentInTheBacen = () ->
            contentIdentifierEventPort.findPixKeyByContentIdentifier(action.getCid())
                .ifPresent(pixKeyInDataBase -> delete(pixKeyInDataBase, false));

        bacenPixKeyByContentIdentifierPort.getPixKey(action.getCid()).ifPresentOrElse(pixKeyPresentInTheBacen, pixKeyNotPresentInTheBacen);
    }

    private void save(final PixKey pixKeyInBacen, final boolean generateLogEvent) {
        createPixKeyPort.createPixKey(pixKeyInBacen, CreateReason.RECONCILIATION);
        log.info("FailureReconciliationSync_hasInBacenAndNotHaveInDatabase {} {}",
            kv(LOG_START_TIME, startCurrentTimeMillis),
            kv("createPixKey", pixKeyInBacen.getKey()));

        if (generateLogEvent) {
            var event = ContentIdentifierEvent.builder()
                .contentIdentifierType(ContentIdentifierEvent.ContentIdentifierEventType.ADDED)
                .cid(pixKeyInBacen.getCid())
                .eventOnBacenAt(LocalDateTime.now(ZoneOffset.UTC))
                .key(pixKeyInBacen.getKey())
                .keyType(pixKeyInBacen.getType())
                .build();

            contentIdentifierEventPort.save(event);
        }
    }

    private void update(final PixKey pixKeyInBacen, final PixKey pixKeyInDataBase, final boolean generateLogEvent) {
        updateAccountPixKeyPort.updateAccount(pixKeyInBacen, UpdateReason.RECONCILIATION);
        log.info("FailureReconciliationSync_hasInBacenAndNotHaveInDatabase {} {}",
            kv(LOG_START_TIME, startCurrentTimeMillis),
            kv("updateAccountPixKey", pixKeyInBacen.getKey()));

        if (generateLogEvent) {
            if (!pixKeyInBacen.getCid().equals(pixKeyInDataBase.getCid())) {
                var oldValue = ContentIdentifierEvent.builder()
                    .contentIdentifierType(ContentIdentifierEvent.ContentIdentifierEventType.REMOVED)
                    .cid(pixKeyInDataBase.getCid())
                    .eventOnBacenAt(LocalDateTime.now(ZoneOffset.UTC))
                    .key(pixKeyInBacen.getKey())
                    .keyType(pixKeyInBacen.getType())
                    .build();
                contentIdentifierEventPort.save(oldValue);
            }

            var newValue = ContentIdentifierEvent.builder()
                .contentIdentifierType(ContentIdentifierEvent.ContentIdentifierEventType.ADDED)
                .cid(pixKeyInBacen.getCid())
                .eventOnBacenAt(LocalDateTime.now(ZoneOffset.UTC))
                .key(pixKeyInBacen.getKey())
                .keyType(pixKeyInBacen.getType())
                .build();
            contentIdentifierEventPort.save(newValue);
        }
    }

    private void delete(final PixKey pixKeyInDatabase, final boolean generateLogEvent) {
        removePixKeyPort.remove(pixKeyInDatabase.getKey(), pixKeyInDatabase.getIspb());
        log.info("FailureReconciliationSync_hasInDatabaseAndNotHaveInBacen {} {}",
            kv(LOG_START_TIME, startCurrentTimeMillis),
            kv("removePixKey", pixKeyInDatabase.getKey()));

        if (generateLogEvent) {
            var event = ContentIdentifierEvent.builder()
                .contentIdentifierType(ContentIdentifierEvent.ContentIdentifierEventType.REMOVED)
                .cid(pixKeyInDatabase.getCid())
                .eventOnBacenAt(LocalDateTime.now(ZoneOffset.UTC))
                .key(pixKeyInDatabase.getKey())
                .keyType(pixKeyInDatabase.getType())
                .build();

            contentIdentifierEventPort.save(event);
        }
    }

}
