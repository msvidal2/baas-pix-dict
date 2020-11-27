package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.domain.Vsync;
import com.picpay.banking.pix.core.domain.Vsync.Action;
import com.picpay.banking.pix.core.ports.reconciliation.BacenReconciliationPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseReconciliationPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class FailureReconciliationSyncUseCase {

    private BacenReconciliationPort bacenReconciliationPort;
    private DatabaseReconciliationPort databaseReconciliationPort;
    private CreatePixKeyUseCase createPixKeyUseCase;
    private RemovePixKeyUseCase removePixKeyUseCase;

    public void execute(Vsync vsync) {
        List<ContentIdentifier> contentIdentifierEvents = bacenReconciliationPort
            .list(vsync.getKeyType(), vsync.getSynchronizedAt(), LocalDateTime.now());

        List<ContentIdentifier> contentIdentifiers = databaseReconciliationPort
            .listAfterLastSuccessfulVsync(vsync.getKeyType(), vsync.getSynchronizedAt());

        Set<Action> actions = vsync.identifyActions(contentIdentifierEvents, contentIdentifiers);

        actions.stream().filter(action -> action.getActionType().equals(Vsync.ActionType.ADD))
            .forEach(action -> createPixKeyUseCase.execute(
                UUID.randomUUID().toString(),
                bacenReconciliationPort.getPixKey(action.getCid()),
                CreateReason.CLIENT_REQUEST)); // TODO: Aqui tem que passar o motivo da criação como RECONCILIATION

        actions.stream().filter(action -> action.getActionType().equals(Vsync.ActionType.REMOVE))
            .forEach(action -> removePixKeyUseCase.execute(
                UUID.randomUUID().toString(),
                bacenReconciliationPort.getPixKey(action.getCid()),
                RemoveReason.CLIENT_REQUEST)); // TODO: Aqui tem que passar o motivo da criação como RECONCILIATION
    }

}
