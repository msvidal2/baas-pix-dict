package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.CreateReason;
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
import java.util.Set;

import static com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction.ActionType;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class FailureReconciliationSyncUseCase {

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
            kv("startCurrentTimeMillis", startCurrentTimeMillis),
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

        contentIdentifierActions.stream().filter(contentIdentifierAction -> contentIdentifierAction.getActionType().equals(ActionType.ADD))
            .forEach(this::hasInBacenAndNotHaveInDatabase);

        contentIdentifierActions.stream().filter(contentIdentifierAction -> contentIdentifierAction.getActionType().equals(ActionType.REMOVE))
            .forEach(this::hasInDatabaseAndNotHaveInBacen);

        syncVerifierHistoricActionPort.saveAll(contentIdentifierActions);
        log.info("FailureReconciliationSync_contentIdentifierActions {} {} {}",
            kv("startCurrentTimeMillis", startCurrentTimeMillis),
            kv("contentIdentifierActions", contentIdentifierActions.size()),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / 1000));
    }

    private void hasInBacenAndNotHaveInDatabase(SyncVerifierHistoricAction action) {
        var pixKeyInBacen = bacenPixKeyByContentIdentifierPort.getPixKey(action.getCid());

        if (pixKeyInBacen.isEmpty()) {
            throw new ReconciliationsException(String.format(
                "The CID %s was identified as ADD in the reconciliation process, but is not present in the CID link query in the DICT API",
                action.getCid()));
        }

        var pixKey = pixKeyInBacen.get();

        var pixKeyInDataBase = findPixKeyPort.findPixKey(pixKeyInBacen.get().getKey());
        if (pixKeyInDataBase.isPresent()) {
            updateAccountPixKeyPort.updateAccount(pixKey, UpdateReason.RECONCILIATION);
            log.info("FailureReconciliationSync_hasInBacenAndNotHaveInDatabase {} {}",
                kv("startCurrentTimeMillis", startCurrentTimeMillis),
                kv("updateAccountPixKey", pixKey.getKey()));
        } else {
            createPixKeyPort.createPixKey(pixKey, CreateReason.RECONCILIATION);
            log.info("FailureReconciliationSync_hasInBacenAndNotHaveInDatabase {} {}",
                kv("startCurrentTimeMillis", startCurrentTimeMillis),
                kv("createPixKey", pixKey.getKey()));
        }
    }

    private void hasInDatabaseAndNotHaveInBacen(final SyncVerifierHistoricAction action) {
        var pixKeyInBacen = bacenPixKeyByContentIdentifierPort.getPixKey(action.getCid());

        if (pixKeyInBacen.isPresent()) {
            throw new ReconciliationsException(String.format(
                "The CID %s was identified as REMOVE in the reconciliation process, but is present in the CID link query in the DICT API",
                action.getCid()));
        }

        var pixKeyInDatabase = contentIdentifierEventPort.findPixKeyByContentIdentifier(action.getCid());
        pixKeyInDatabase.ifPresent(pixKey -> {
            removePixKeyPort.remove(pixKey.getKey(), pixKey.getIspb());
            log.info("FailureReconciliationSync_hasInDatabaseAndNotHaveInBacen {} {}",
                kv("startCurrentTimeMillis", startCurrentTimeMillis),
                kv("removePixKey", pixKey.getKey()));
        });
    }

}
