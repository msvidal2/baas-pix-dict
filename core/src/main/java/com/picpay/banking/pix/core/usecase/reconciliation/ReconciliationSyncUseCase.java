package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Vsync;
import com.picpay.banking.pix.core.ports.reconciliation.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseVsyncPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class ReconciliationSyncUseCase {

    private DatabaseVsyncPort databaseVsyncPort;
    private DatabaseContentIdentifierPort databaseContentIdentifierPort;
    private BacenSyncVerificationsPort bacenSyncVerificationsPort;
    private FailureReconciliationSyncUseCase failureReconciliationSyncUseCase;

    public void execute() {
        // TODO: Esse usecase tem que rodar em horário agendado

        List.of(KeyType.values()).forEach(keyType -> {
            var vsync = databaseVsyncPort.getLastSuccessfulVsync(keyType);
            if (vsync == null) {
                vsync = Vsync.builder()
                    .keyType(keyType)
                    .synchronizedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .build();
            }

            List<ContentIdentifier> contentIdentifiers = databaseContentIdentifierPort.listAfterLastSuccessfulVsync(vsync.getKeyType(),
                vsync.getSynchronizedAt());

            vsync.calculateVsync(contentIdentifiers);

            var result = bacenSyncVerificationsPort.syncVerification(vsync.getVsync());

            vsync.syncVerificationResult(result);

            databaseVsyncPort.updateVerificationResult(vsync);

            if (vsync.getVsyncResult().equals(Vsync.VsyncResult.NOK)) {
                failureReconciliationSyncUseCase.execute(vsync);
            }
        });
    }

}
