/*
 *  baas-pix-dict 1.0 17/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationUseCase;
import com.picpay.banking.pix.core.validators.reconciliation.lock.UnavailableWhileSyncIsActive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rafael.braga
 * @version 1.0 17/02/2021
 */
@RestController
@RequestMapping(value = "v1/sync")
@RequiredArgsConstructor
@Slf4j
@UnavailableWhileSyncIsActive
public class ReconciliationController {

    private final FailureReconciliationSyncByFileUseCase syncByCidsFileUseCase;
    private final ReconciliationUseCase reconciliationUseCase;

    @Trace(dispatcher = true, metricName = "manual_file_sync")
    @PostMapping("file/{keyType}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void startSyncByFile(@PathVariable("keyType") KeyType keyType) {
        log.info("Iniciando processo de sincronizacao por arquivo com chave {}. Chamada MANUAL", keyType);
        syncByCidsFileUseCase.execute(keyType);
    }

    @Trace(dispatcher = true, metricName = "full_manual_sync")
    @PostMapping("full/{keyType}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void startFullSync(@PathVariable("keyType") KeyType keyType) {
        log.info("Iniciando processo de sincronizacao completo por chave {}. Chamada MANUAL", keyType);
        reconciliationUseCase.execute(keyType);
    }


}