package com.picpay.banking.pix.dict.syncverifier.task;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.dict.syncverifier.service.SyncVerifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class SyncVerificationTask implements ApplicationRunner {

    private final SyncVerifierService syncVerifierService;

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
    public void run(final ApplicationArguments args) throws InterruptedException {
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
            if (!service.awaitTermination(60, TimeUnit.MINUTES)) {
                log.error("SyncApplication took more than 60 minutes and was interrupted");
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("SyncApplication was interrupted {}", e.getMessage());
            service.shutdownNow();
            throw e;
        }
    }

    private void runByKeyType(KeyType syncKeyType) {
        log.info("SyncApplication start: {}", syncKeyType.name());
        SyncVerifierHistoric syncVerifierHistoric = syncVerifierService.syncVerifier(syncKeyType);

        if (!onlySyncVerifier && syncVerifierHistoric.isNOK()) {
            syncVerifierService.failureReconciliationSync(syncVerifierHistoric);
        }

        log.info("SyncApplication end: {}", syncVerifierHistoric);
    }

}
