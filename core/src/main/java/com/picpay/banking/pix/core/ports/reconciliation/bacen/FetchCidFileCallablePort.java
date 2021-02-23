/*
 *  baas-pix-dict 1.0 12/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.ResultCidFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * @author rafael.braga
 * @version 1.0 12/02/2021
 */
@RequiredArgsConstructor
@Slf4j
public class FetchCidFileCallablePort implements SafeCallable<Optional<ResultCidFile>> {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final Integer fileId;

    @Override
    public Optional<ResultCidFile> call() {
        log.info("Verificando se o arquivo {} ja esta disponivel...", fileId);
        final var availableFile = this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(fileId);

        if (availableFile == null || availableFile.getStatus().isNotAvailable()) {
            log.info("Arquivo {} ainda nao disponivel", kv("contentIdentifierFileId", fileId));
            return Optional.empty();
        }

        final var keyType = availableFile.getKeyType();
        log.info("Arquivo encontrado. Retornando para inicio da sync por arquivo... ID arquivo: {} KeyType: {}", kv("contentIdentifierFileId", fileId), kv("keyType", keyType));
        final var cids = this.bacenContentIdentifierEventsPort.downloadCidsFromBacen(availableFile.getUrl());
        return Optional.of(new ResultCidFile(cids));
    }

}
