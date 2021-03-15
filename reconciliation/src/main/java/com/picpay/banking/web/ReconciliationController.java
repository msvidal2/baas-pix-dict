/*
 *  baas-pix-dict 1.0 17/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierResultType;
import com.picpay.banking.pix.core.usecase.reconciliation.FileReconciliationCheckUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.SincronizeCIDEventsUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.SyncVerifierUseCase;
import com.picpay.banking.pix.core.validators.reconciliation.lock.UnavailableWhileSyncIsActive;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import com.picpay.banking.reconciliation.dto.response.EntryByCidResponse;
import com.picpay.banking.reconciliation.entity.SyncVerifierHistoricEntity;
import com.picpay.banking.reconciliation.repository.SyncVerifierHistoricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    private final FileReconciliationCheckUseCase syncByCidsFileUseCase;
    private final ReconciliationUseCase reconciliationUseCase;
    private final SyncVerifierUseCase syncVerifierUseCase;
    private final SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase;
    private final BacenReconciliationClient bacenReconciliationClient;
    private final SyncVerifierHistoricRepository syncVerifierHistoricRepository;

    @Trace(dispatcher = true, metricName = "manual_syncByFile")
    @PostMapping("file/{keyType}")
    @ResponseStatus(HttpStatus.OK)
    public SyncVerifierHistoric startSyncByFile(@PathVariable("keyType") KeyType keyType) {
        log.info("Iniciando processo de sincronizacao por arquivo com chave {}. Chamada MANUAL", keyType);

        var failureSyncVerifierHistoric = SyncVerifierHistoric.builder()
            .vsyncStart("MANUAL")
            .vsyncEnd("MANUAL")
            .synchronizedStart(LocalDateTime.now())
            .synchronizedEnd(LocalDateTime.now())
            .keyType(keyType)
            .syncVerifierResultType(SyncVerifierResultType.NOK)
            .build();

        var savedSyncVerifierHistoric = syncVerifierHistoricRepository
            .save(SyncVerifierHistoricEntity.from(failureSyncVerifierHistoric)).toDomain();

        return syncByCidsFileUseCase.execute(savedSyncVerifierHistoric);
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
    public Map<String, SyncVerifierHistoric> startAllKeyTypeWithFullSync() {
        Map<String, SyncVerifierHistoric> result = new HashMap<>();
        Arrays.stream(KeyType.values()).forEach(keyType -> result.put(keyType.name(), reconciliationUseCase.execute(keyType)));

        return result;
    }

    @Trace(dispatcher = true, metricName = "manual_onlySyncVerifier")
    @PostMapping("onlyVerifier/{keyType}")
    @ResponseStatus(HttpStatus.OK)
    public SyncVerifierHistoric startOnlySyncVerifier(@PathVariable("keyType") KeyType keyType) {
        log.info("Iniciando processo manual de verificação de sincronismo sem alteração nos dados.");
        sincronizeCIDEventsUseCase.syncByKeyType(keyType);
        return syncVerifierUseCase.execute(keyType);
    }

    @Trace(dispatcher = true, metricName = "manual_onlySyncVerifier")
    @GetMapping("cid/{cid}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EntryByCidResponse test(@PathVariable("cid") String cid) {
        return bacenReconciliationClient.getEntryByCid(cid, "22896431");
    }

}