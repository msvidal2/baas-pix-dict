package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierAction;
import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.ReconciliationsException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.ReconciliationBacenPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateAccountPixKeyUseCase;
import com.picpay.banking.pix.core.validators.reconciliation.VsyncHistoricValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.picpay.banking.pix.core.domain.SyncVerifierHistoric.ActionType;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class FailureReconciliationSyncUseCase {

    private final ReconciliationBacenPort reconciliationBacenPort;
    private final ContentIdentifierActionPort contentIdentifierActionPort;
    private final ContentIdentifierEventPort contentIdentifierEventPort;
    private final CreatePixKeyUseCase createPixKeyUseCase;
    private final UpdateAccountPixKeyUseCase updateAccountPixKeyUseCase;
    private final RemovePixKeyUseCase removePixKeyUseCase;
    private final FindPixKeyPort findPixKeyPort;

    private long startCurrentTimeMillis;

    public void execute(SyncVerifierHistoric syncVerifierHistoric) {
        this.startCurrentTimeMillis = System.currentTimeMillis();
        log.info("FailureReconciliationSync_started {} {}",
            kv("startCurrentTimeMillis", startCurrentTimeMillis),
            kv("vsyncHistoric", syncVerifierHistoric));

        VsyncHistoricValidator.validate(syncVerifierHistoric);

        List<ContentIdentifierEvent> bacenEvents = reconciliationBacenPort
            .list(syncVerifierHistoric.getKeyType(), syncVerifierHistoric.getSynchronizedStart(), LocalDateTime.now());

        List<ContentIdentifierEvent> databaseEvents = contentIdentifierEventPort
            .findAllAfterLastSuccessfulVsync(syncVerifierHistoric.getKeyType(), syncVerifierHistoric.getSynchronizedStart());

        var contentIdentifierActions = syncVerifierHistoric.identifyActions(bacenEvents, databaseEvents);
        if (contentIdentifierActions.isEmpty()) {
            throw new ReconciliationsException(
                String.format("No action found for %s type reconciliation", syncVerifierHistoric.getKeyType().name()));
        }

        contentIdentifierActionPort.saveAll(contentIdentifierActions);
        log.info("FailureReconciliationSync_contentIdentifierActions {} {}",
            kv("startCurrentTimeMillis", startCurrentTimeMillis),
            kv("contentIdentifierActions", contentIdentifierActions.size()));

        contentIdentifierActions.stream().filter(contentIdentifierAction -> contentIdentifierAction.getActionType().equals(ActionType.ADD))
            .forEach(this::hasInBacenAndNotHaveInDatabase);

        contentIdentifierActions.stream().filter(contentIdentifierAction -> contentIdentifierAction.getActionType().equals(ActionType.REMOVE))
            .forEach(this::hasInDatabaseAndNotHaveInBacen);

    }

    private void hasInBacenAndNotHaveInDatabase(ContentIdentifierAction action) {
        var pixKeyInBacen = reconciliationBacenPort.getPixKey(action.getCid());

        if (pixKeyInBacen.isEmpty()) {
            throw new ReconciliationsException(String.format(
                "The CID %s was identified as ADD in the reconciliation process, but is not present in the CID link query in the DICT API",
                action.getCid()));
        }

        var pixKey = pixKeyInBacen.get();

        var pixKeyInDataBase = findPixKeyPort.findPixKey(pixKeyInBacen.get().getKey());
        if (pixKeyInDataBase.isPresent()) {
            updateAccountPixKeyUseCase.execute(UUID.randomUUID().toString(), pixKey, UpdateReason.RECONCILIATION);
            log.info("FailureReconciliationSync_hasInBacenAndNotHaveInDatabase {} {}",
                kv("startCurrentTimeMillis", startCurrentTimeMillis),
                kv("updateAccountPixKey", pixKey.getKey()));
        } else {
            createPixKeyUseCase.execute(UUID.randomUUID().toString(), pixKey, CreateReason.RECONCILIATION);
            log.info("FailureReconciliationSync_hasInBacenAndNotHaveInDatabase {} {}",
                kv("startCurrentTimeMillis", startCurrentTimeMillis),
                kv("createPixKey", pixKey.getKey()));
        }
    }

    private void hasInDatabaseAndNotHaveInBacen(final ContentIdentifierAction action) {
        var pixKeyInBacen = reconciliationBacenPort.getPixKey(action.getCid());

        if (pixKeyInBacen.isPresent()) {
            throw new ReconciliationsException(String.format(
                "The CID %s was identified as REMOVE in the reconciliation process, but is present in the CID link query in the DICT API",
                action.getCid()));
        }

        var pixKeyInDatabase = contentIdentifierEventPort.findPixKeyByContentIntentifier(action.getCid());
        if (pixKeyInDatabase.isEmpty()) {
            throw new ReconciliationsException(String.format(
                "The CID %s was identified as REMOVE in the reconciliation process, but is not present in the local database",
                action.getCid()));
        }

        var pixKey = pixKeyInDatabase.get();

        removePixKeyUseCase.execute(UUID.randomUUID().toString(), pixKey, RemoveReason.RECONCILIATION);
        log.info("FailureReconciliationSync_hasInDatabaseAndNotHaveInBacen {} {}",
            kv("startCurrentTimeMillis", startCurrentTimeMillis),
            kv("removePixKey", pixKey.getKey()));
    }

}
