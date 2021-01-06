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
    public void run(final ApplicationArguments args) {
        JCommander.newBuilder()
            .addObject(this)
            .build()
            .parse(args.getSourceArgs());

        if (keyType != null) {
            runByKeyType(keyType);
        } else {
            Arrays.stream(KeyType.values()).forEach(this::runByKeyType);
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
