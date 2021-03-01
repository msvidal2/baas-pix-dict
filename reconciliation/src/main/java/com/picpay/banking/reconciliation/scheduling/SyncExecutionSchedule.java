package com.picpay.banking.reconciliation.scheduling;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.exception.ReconciliationException;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class SyncExecutionSchedule {

    public static final int TIME = 60;
    private final ReconciliationUseCase reconciliationUseCase;

    @Trace(dispatcher = true, metricName = "syncVerifier")
    @Scheduled(cron = "${picpay.dict.sync.schedule}")
    public void run() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        Arrays.stream(KeyType.values()).forEach(syncKeyType -> service.execute(() -> runByKeyType(syncKeyType)));
        service.shutdown();

        try {
            if (!service.awaitTermination(TIME, TimeUnit.MINUTES)) {
                log.error("SyncApplication levou mais que {} minutos e foi interrompida", TIME);
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("SyncApplication was interrupted {}", e.getMessage());
            Thread.currentThread().interrupt();
            service.shutdownNow();
            throw new ReconciliationException("Sincronismo agendado foi interrompido", e);
        }
    }

    private void runByKeyType(KeyType syncKeyType) {
        reconciliationUseCase.execute(syncKeyType);
    }

}
