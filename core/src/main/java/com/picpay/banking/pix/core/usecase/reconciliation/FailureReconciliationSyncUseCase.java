package com.picpay.banking.pix.core.usecase.reconciliation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class FailureReconciliationSyncUseCase {

    public void execute() {
        // tem que solicitar o arquivo para o Bacen
        // fazer pooling para aguardar a geração do arquivo até que ele esteja disponível
        // com o arquivo em mães temos duas situações:
        //   1. A chave existe no picpay e não existe no Bacen
        //      Resposta: re-enviar para o bacen o cadastro
        //   2. A chave existe no bacen e não existe no picpay
        //      Resposta: chamar endpoint "Consultar Vínculo por CID" e persistir em nossa base o cadastro
    }

}
