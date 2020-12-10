package com.picpay.banking.pix.dict.syncverifier.task;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.dict.syncverifier.service.SyncVerifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class SyncVerificationTask implements CommandLineRunner {

    private final SyncVerifierService syncVerifierService;

    @Parameter(names = "-keyType",
        description = "Type of key used for sync: CPF, CNPJ, EMAIL, CELLPHONE, RANDOM.",
        converter = CommandLineKeyTypeConverter.class,
        required = true
    )
    private KeyType keyType;

    @Override
    public void run(final String... args) {
        JCommander.newBuilder()
            .addObject(this)
            .build()
            .parse(args);

        log.info("SyncApplication start: {}", keyType.name());
        SyncVerifierHistoric syncVerifierHistoric = syncVerifierService.syncVerifier(keyType);
        if (syncVerifierHistoric.isNOK()) {
            syncVerifierService.failureReconciliationSync(syncVerifierHistoric);
            syncVerifierHistoric = syncVerifierService.syncVerifier(keyType);
        }
        log.info("SyncApplication end: {}", syncVerifierHistoric);
    }

}
