/*
 *  baas-pix-dict 1.0 17/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationSyncUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.SincronizeCIDEventsUseCase;
import com.picpay.banking.pix.core.validators.reconciliation.lock.UnavailableWhileSyncIsActive;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

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
    private final ReconciliationSyncUseCase reconciliationSyncUseCase;
    private final SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase;
    private final BacenReconciliationClient bacenReconciliationClient;

    @Trace(dispatcher = true, metricName = "manual_syncByFile")
    @PostMapping("file/{keyType}")
    @ResponseStatus(HttpStatus.OK)
    public SyncVerifierHistoric startSyncByFile(@PathVariable("keyType") KeyType keyType) {
        log.info("Iniciando processo de sincronizacao por arquivo com chave {}. Chamada MANUAL", keyType);
        return syncByCidsFileUseCase.execute(keyType);
    }

    @Trace(dispatcher = true, metricName = "manual_fullSync")
    @PostMapping("full/{keyType}")
    @ResponseStatus(HttpStatus.OK)
    public SyncVerifierHistoric startFullSync(@PathVariable("keyType") KeyType keyType) {
        log.info("Iniciando processo de sincronizacao completo por chave {}. Chamada MANUAL", keyType);
        return reconciliationUseCase.execute(keyType);
    }

    @Trace(dispatcher = true, metricName = "manual_allKeyTypeWithFullSync")
    @PostMapping("full")
    @ResponseStatus(HttpStatus.OK)
    public void startAllKeyTypeWithFullSync() {
        Arrays.stream(KeyType.values()).forEach(reconciliationUseCase::execute);
    }

    @Trace(dispatcher = true, metricName = "manual_onlySyncVerifier")
    @PostMapping("onlyVerifier/{keyType}")
    @ResponseStatus(HttpStatus.OK)
    public SyncVerifierHistoric startOnlySyncVerifier(@PathVariable("keyType") KeyType keyType) {
        log.info("Iniciando processo manual de verificação de sincronismo sem alteração nos dados.");
        sincronizeCIDEventsUseCase.syncByKeyType(keyType);
        return reconciliationSyncUseCase.execute(keyType);
    }

}