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


@Slf4j
@Component
@RequiredArgsConstructor
public class SyncVerificationTask implements ApplicationRunner {

    private final SyncVerifierService syncVerifierService;

    @Parameter(names = "-keyType",
        description = "Type of key used for sync: CPF, CNPJ, EMAIL, CELLPHONE, RANDOM.",
        converter = CommandLineKeyTypeConverter.class,
        required = true
    )
    private KeyType keyType;

    @Parameter(names = "-onlySyncVerifier",
        description = "Defines that only the sync check will be done and no data correction will be applied: True or False"
    )
    private boolean onlySyncVerifier = false;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        JCommander.newBuilder()
            .addObject(this)
            .build()
            .parse(args.getSourceArgs());

        log.info("SyncApplication start: {}", keyType.name());
        SyncVerifierHistoric syncVerifierHistoric = syncVerifierService.syncVerifier(keyType);

        if (!onlySyncVerifier && syncVerifierHistoric.isNOK()) {
            syncVerifierService.failureReconciliationSync(syncVerifierHistoric);
        }

        log.info("SyncApplication end: {}", syncVerifierHistoric);
    }

}
