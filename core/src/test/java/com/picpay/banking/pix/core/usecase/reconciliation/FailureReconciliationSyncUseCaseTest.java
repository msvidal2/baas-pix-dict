package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierAction;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Vsync;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseVsyncPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import org.junit.jupiter.api.BeforeEach;
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
public class FailureReconciliationSyncUseCaseTest {

    @Mock
    private DatabaseVsyncPort databaseVsyncPort;
    @Mock
    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    @Mock
    private BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    @Mock
    private CreatePixKeyUseCase createPixKeyUseCase;
    @Mock
    private RemovePixKeyUseCase removePixKeyUseCase;

    private ContentIdentifierAction cidAdd;
    private ContentIdentifierAction cidRemove;
    private PixKey pixAdd;
    private PixKey pixRemove;

    @BeforeEach
    void setup() {
        cidAdd = ContentIdentifierAction.builder()
            .actionType(ContentIdentifierAction.ActionType.ADD)
            .contentIdentifier(ContentIdentifier.builder()
                .cid("1")
                .key("1")
                .keyType(KeyType.CELLPHONE)
                .build())
            .build();

        cidRemove = ContentIdentifierAction.builder()
            .actionType(ContentIdentifierAction.ActionType.REMOVE)
            .contentIdentifier(ContentIdentifier.builder()
                .cid("2")
                .key("2")
                .keyType(KeyType.CELLPHONE)
                .build())
            .build();

        pixAdd = PixKey.builder().key("1").build();

        pixRemove = PixKey.builder().key("2").build();
    }

    @Test
    public void when_success_expect_produce_create_and_remove_events() {
        when(databaseVsyncPort.getLastSuccessfulVsync(any()))
            .thenReturn(Vsync.builder().build());
        when(bacenContentIdentifierEventsPort.list(any(), any(), any()))
            .thenReturn(List.of(cidAdd, cidRemove));
        when(bacenPixKeyByContentIdentifierPort.getPixKey(cidAdd.getContentIdentifier()))
            .thenReturn(pixAdd);
        when(bacenPixKeyByContentIdentifierPort.getPixKey(cidRemove.getContentIdentifier()))
            .thenReturn(pixRemove);

        FailureReconciliationSyncUseCase failureReconciliationSyncUseCase = new FailureReconciliationSyncUseCase(
            databaseVsyncPort,
            bacenContentIdentifierEventsPort,
            bacenPixKeyByContentIdentifierPort,
            createPixKeyUseCase,
            removePixKeyUseCase
        );

        failureReconciliationSyncUseCase.execute(KeyType.CELLPHONE);

        ArgumentCaptor<PixKey> pixKeyAddArgumentCaptor = ArgumentCaptor.forClass(PixKey.class);
        verify(createPixKeyUseCase).execute(any(), pixKeyAddArgumentCaptor.capture(), any());
        assertThat(pixKeyAddArgumentCaptor.getValue().getKey()).isEqualTo(pixAdd.getKey());

        ArgumentCaptor<PixKey> pixKeyRemovedArgumentCaptor = ArgumentCaptor.forClass(PixKey.class);
        verify(removePixKeyUseCase).execute(any(), pixKeyRemovedArgumentCaptor.capture(), any());
        assertThat(pixKeyRemovedArgumentCaptor.getValue().getKey()).isEqualTo(pixRemove.getKey());
    }

}
