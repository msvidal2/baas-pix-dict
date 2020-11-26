package com.picpay.banking.pix.core.usecase.reconciliation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CidProviderUseCase {

    public void execute() {
        // recebe um evento que vem da fila do kafka
        // faz o calculo de cid
        // persiste o cid no banco de dados (uma tabela de CidS)
        // pensar em estratégia para guardar o histórico de alterações de cid
    }

}
