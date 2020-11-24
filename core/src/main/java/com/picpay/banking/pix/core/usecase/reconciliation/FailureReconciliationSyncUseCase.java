package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierAction;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseVsyncPort;
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

    private DatabaseVsyncPort databaseVsyncPort;
    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private CreatePixKeyUseCase createPixKeyUseCase;
    private RemovePixKeyUseCase removePixKeyUseCase;

    public void execute(KeyType keyType) {
        // tem que solicitar o arquivo para o Bacen
        // fazer pooling para aguardar a geração do arquivo até que ele esteja disponível
        // com o arquivo em mães temos duas situações:
        //   1. A chave existe no picpay e não existe no Bacen
        //      Resposta: re-enviar para o bacen o cadastro
        //   2. A chave existe no bacen e não existe no picpay
        //      Resposta: chamar endpoint "Consultar Vínculo por CID" e persistir em nossa base o cadastro

        LocalDateTime startTime = null;
        LocalDateTime endTime = LocalDateTime.now();

        var lastSuccessfulVsync = databaseVsyncPort.getLastSuccessfulVsync(keyType);
        if (lastSuccessfulVsync == null) {
            startTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        } else {
            startTime = lastSuccessfulVsync.getSynchronizedAt();
        }

        List<ContentIdentifierAction> contentIdentifiers = bacenContentIdentifierEventsPort.list(keyType, startTime, endTime);
        contentIdentifiers.stream().filter(cid -> cid.getActionType().equals(ContentIdentifierAction.ActionType.ADD))
            .forEach(cid -> createPixKeyUseCase.execute(
                UUID.randomUUID().toString(),
                bacenPixKeyByContentIdentifierPort.getPixKey(cid.getContentIdentifier()),
                CreateReason.CLIENT_REQUEST)); // TODO: Aqui tem que passar o motivo da criação como RECONCILIATION

        contentIdentifiers.stream().filter(cid -> cid.getActionType().equals(ContentIdentifierAction.ActionType.REMOVE))
            .forEach(cid -> removePixKeyUseCase.execute(
                UUID.randomUUID().toString(),
                bacenPixKeyByContentIdentifierPort.getPixKey(cid.getContentIdentifier()),
                RemoveReason.CLIENT_REQUEST)); // TODO: Aqui tem que passar o motivo da exclusão como RECONCILIATION
    }

}
