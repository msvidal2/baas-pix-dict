/*
 *  baas-pix-dict 1.0 11/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.ResultCidFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author rafael.braga
 * @version 1.0 11/02/2021
 */
@Slf4j
@RequiredArgsConstructor
public class PollCidFilePort {

    private final BlockingQueue<ResultCidFile> resultQueue = new ArrayBlockingQueue<>(1, true);
    private ScheduledFuture<?> future;

    public Optional<ResultCidFile> poll(SafeCallable<Optional<ResultCidFile>> pollee,
                                        int period, TimeUnit periodTimeUnit,
                                        int timeout, TimeUnit timeoutTimeUnit) {
        log.info("Iniciando polling de arquivo do BACEN com intervalo de {} {}", period, periodTimeUnit);
        future = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> poll(pollee), 0, period, periodTimeUnit);
        final Optional<ResultCidFile> resultOpt = Optional.ofNullable(get(timeout, timeoutTimeUnit));
        if (resultOpt.isEmpty()) {
            log.error("Time out ao buscar aquivo no bacen. Tempo limite excedido. ");
        }
        return resultOpt;
    }

    private void poll(SafeCallable<Optional<ResultCidFile>> pollable) {
        final Optional<ResultCidFile> opt = pollable.call();
        if (opt.isPresent()) {
            final ResultCidFile result = opt.get();
            log.info("Encontrado arquivo no BACEN. Retornando arquivo para continuar processamento");
            put(result);
        } else {
            log.info("Arquivo ainda nao disponivel");
        }
    }

    private ResultCidFile get(int timeout, TimeUnit timeoutTimeUnit) {
        try {
            return resultQueue.poll(timeout, timeoutTimeUnit);
        } catch (InterruptedException e) {
            log.error("Thread de polling interrompida ao buscar arquivo", e);
            Thread.currentThread().interrupt();
            return null;
        } finally {
            stop();
        }
    }

    private void put(ResultCidFile result) {
        try {
            resultQueue.put(result);
        } catch (InterruptedException e) {
            log.error("Thread de polling interrompida ao guardar arquivo", e);
            Thread.currentThread().interrupt();
        } finally {
            stop();
        }
    }

    private void stop() {
        if (future != null) {
            future.cancel(true);
        }
        log.info("Encerrando polling");
    }

}
