package com.picpay.banking.pix.sync.task;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.sync.service.SyncVerifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncVerificationTask implements ApplicationRunner {

    private final SyncVerifierService syncVerifierService;

    @Parameter(names = "-keyType",
        description = "Type of key used for sync: CPF, CNPJ, EMAIL, CELLPHONE, RANDOM.",
        converter = CommandLineKeyTypeConverter.class,
        required = true
    )
    private KeyType keyType;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        JCommander.newBuilder()
            .addObject(this)
            .build()
            .parse(args.getSourceArgs());

        log.info("SyncApplication start: {}", keyType.name());
        SyncVerifierHistoric syncVerifierHistoric = syncVerifierService.syncVerifier(keyType);
        if (syncVerifierHistoric.isNOK()) {
            syncVerifierService.failureReconciliationSync(syncVerifierHistoric);
            syncVerifierHistoric = syncVerifierService.syncVerifier(keyType);
        }
        log.info("SyncApplication end: {}", syncVerifierHistoric);
    }

}
