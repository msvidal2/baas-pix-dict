package com.picpay.banking.reconciliation.task;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import com.picpay.banking.reconciliation.service.SincronizeCidEventsScheduler;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import com.picpay.banking.reconciliation.service.SyncVerifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.logstash.logback.argument.StructuredArguments.kv;


@Slf4j
@Component
@RequiredArgsConstructor
public class SyncVerificationTask implements ApplicationRunner {

    public static final int TIME = 60;
    private final SyncVerifierService syncVerifierService;
    private final ReconciliationLockPort lockPort;
    private final SincronizeCidEventsScheduler sincronizeCidEventsScheduler;
    private final FailureReconciliationSyncByFileUseCase syncByFileUseCase;

    @Parameter(names = "-keyType",
        description = "Type of key used for sync: CPF, CNPJ, EMAIL, CELLPHONE, RANDOM.",
        converter = CommandLineKeyTypeConverter.class
    )
    private KeyType keyType;

    @Parameter(names = "-onlySyncVerifier",
        description = "Defines that only the sync check will be done and no data correction will be applied: True or False"
    )
    private boolean onlySyncVerifier = false;

    @Override
    public void run(final ApplicationArguments args) {
        try {
            lockPort.lock();
            runReconciliation(args);
        } finally {
            lockPort.unlock();
        }
    }

    private void runReconciliation(final ApplicationArguments args) {
        JCommander.newBuilder()
            .addObject(this)
            .build()
            .parse(args.getSourceArgs());

        ExecutorService service = Executors.newFixedThreadPool(1);

        if (keyType != null) {
            service.execute(() -> runByKeyType(keyType));
        } else {
            Arrays.stream(KeyType.values())
                .forEach(syncKeyType -> service.execute(() -> runByKeyType(syncKeyType)));
        }

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
            throw new IllegalArgumentException(e);
        }
    }

    private void runByKeyType(KeyType syncKeyType) {
        sincronizeCidEventsScheduler.runByKeyType(syncKeyType);

        SyncVerifierHistoric syncVerifierHistoric = syncVerifierService.syncVerifier(syncKeyType);

        if (!onlySyncVerifier && syncVerifierHistoric.isNOK()) {
            SyncVerifierHistoric executedHistoric = syncVerifierService.failureReconciliationSync(syncVerifierHistoric);

            if (executedHistoric.isNOK()) {
                log.warn("Sync NOK com eventos eventos. Iniciando tratamento por arquivos. Horario: {}", LocalDateTime.now());
                syncByFileUseCase.execute(syncKeyType);
            }
        }

        log.info("Termino da sync end: {}, {}", kv("keyType", syncKeyType.name()), kv("syncVerifierHistoric", syncVerifierHistoric));
    }

}
