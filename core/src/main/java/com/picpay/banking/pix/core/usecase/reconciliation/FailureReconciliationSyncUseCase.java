package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.domain.Vsync;
import com.picpay.banking.pix.core.domain.Vsync.Action;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class FailureReconciliationSyncUseCase {

    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private CreatePixKeyUseCase createPixKeyUseCase;
    private RemovePixKeyUseCase removePixKeyUseCase;

    public void execute(Vsync vsync) {
        List<ContentIdentifierEvent> contentIdentifierEvents = bacenContentIdentifierEventsPort
            .list(vsync.getKeyType(), vsync.getSynchronizedAt(), LocalDateTime.now());

        List<Action> actions = vsync.identifyActions(contentIdentifierEvents);

        actions.stream().filter(action -> action.getActionType().equals(Vsync.ActionType.ADD))
            .forEach(action -> createPixKeyUseCase.execute(
                UUID.randomUUID().toString(),
                bacenPixKeyByContentIdentifierPort.getPixKey(action.getCid()),
                CreateReason.CLIENT_REQUEST)); // TODO: Aqui tem que passar o motivo da criação como RECONCILIATION

        actions.stream().filter(action -> action.getActionType().equals(Vsync.ActionType.REMOVE))
            .forEach(action -> removePixKeyUseCase.execute(
                UUID.randomUUID().toString(),
                bacenPixKeyByContentIdentifierPort.getPixKey(action.getCid()),
                RemoveReason.CLIENT_REQUEST)); // TODO: Aqui tem que passar o motivo da criação como RECONCILIATION
    }

}
