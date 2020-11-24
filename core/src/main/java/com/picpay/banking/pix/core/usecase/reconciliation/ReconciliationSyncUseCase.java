package com.picpay.banking.pix.core.usecase.reconciliation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ReconciliationSyncUseCase {

    public void execute() {
        // roda via job em horário agendado
        // primeira vez: fazer carga com a base atual para gerar o primeiro vsync
        //
        // para cada grupo é feito o calculo do vsync
        // transmitir o vsync para o Bacen. Ele vai retornar OK ou NOK
        // Se caso NOK chamar usecase FailureReconciliationSyncUseCase
    }

}
