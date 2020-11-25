package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Vsync;
import com.picpay.banking.pix.core.ports.reconciliation.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseVsyncPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReconciliationSyncUseCaseTest {

    @Mock
    private DatabaseVsyncPort databaseVsyncPort;
    @Mock
    private DatabaseContentIdentifierPort databaseContentIdentifierPort;
    @Mock
    private BacenSyncVerificationsPort bacenSyncVerificationsPort;
    @Mock
    private FailureReconciliationSyncUseCase failureReconciliationSyncUseCase;

    @Test
    public void success() {
        when(databaseVsyncPort.getLastSuccessfulVsync(any()))
            .thenReturn(Vsync.builder().build());

        when(databaseContentIdentifierPort.listAfterLastSuccessfulVsync(any(), any()))
            .thenReturn(List.of(
                ContentIdentifier.builder()
                    .cid("1")
                    .build()));

        ReconciliationSyncUseCase reconciliationSyncUseCase = new ReconciliationSyncUseCase(
            databaseVsyncPort,
            databaseContentIdentifierPort,
            bacenSyncVerificationsPort,
            failureReconciliationSyncUseCase);

        reconciliationSyncUseCase.execute();

        //ArgumentCaptor<PixKey> vsyncArgumentCaptor = ArgumentCaptor.forClass(PixKey.class);
        //verify(failureReconciliationSyncUseCase).execute(vsyncArgumentCaptor.capture());
        //assertThat(vsyncArgumentCaptor.getValue().getKey()).isEqualTo(pixAdd.getKey());
    }

}
