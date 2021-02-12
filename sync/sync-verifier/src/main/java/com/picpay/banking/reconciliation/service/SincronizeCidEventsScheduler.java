package com.picpay.banking.reconciliation.service;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.usecase.reconciliation.SincronizeCIDEventsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class SincronizeCidEventsScheduler {

    private static final String EVERY_HOUR = "0 0 0/1 1/1 * ?";
    private final SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase;

    @Transactional
    @Scheduled(cron = EVERY_HOUR)
    @Trace(dispatcher = true, metricName = "sincronizeCidEventsScheduler_all")
    public void run() {
        Arrays.stream(KeyType.values()).forEach(sincronizeCIDEventsUseCase::syncByKeyType);
    }

    @Transactional
    @Trace(dispatcher = true, metricName = "sincronizeCidEventsScheduler_keyType")
    public void runByKeyType(KeyType keyType) {
        sincronizeCIDEventsUseCase.syncByKeyType(keyType);
    }

}
