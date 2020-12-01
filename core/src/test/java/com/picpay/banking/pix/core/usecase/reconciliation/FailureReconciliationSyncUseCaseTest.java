package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Vsync;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FailureReconciliationSyncUseCaseTest {

    @Mock
    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    @Mock
    private BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    @Mock
    private CreatePixKeyUseCase createPixKeyUseCase;
    @Mock
    private RemovePixKeyUseCase removePixKeyUseCase;

    private PixKey pixAdd;
    private PixKey pixRemove;

    @BeforeEach
    void setup() {
        pixAdd = PixKey.builder().key("1").build();
        pixRemove = PixKey.builder().key("2").build();
    }

    @Test
    public void when_success_expect_produce_create_and_remove_events() {
        when(bacenContentIdentifierEventsPort.list(any(), any(), any()))
            .thenReturn(List.of(
                ContentIdentifierEvent.builder()
                    .eventType(ContentIdentifierEvent.EventType.ADD)
                    .cid("1")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifierEvent.builder()
                    .eventType(ContentIdentifierEvent.EventType.REMOVE)
                    .cid("2")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifierEvent.builder()
                    .eventType(ContentIdentifierEvent.EventType.REMOVE)
                    .cid("1")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifierEvent.builder()
                    .eventType(ContentIdentifierEvent.EventType.ADD)
                    .cid("1")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifierEvent.builder()
                    .eventType(ContentIdentifierEvent.EventType.ADD)
                    .cid("2")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifierEvent.builder()
                    .eventType(ContentIdentifierEvent.EventType.REMOVE)
                    .cid("2")
                    .dateTime(LocalDateTime.now())
                    .build()));

        when(bacenPixKeyByContentIdentifierPort.getPixKey("1"))
            .thenReturn(Optional.of(pixAdd));
        when(bacenPixKeyByContentIdentifierPort.getPixKey("2"))
            .thenReturn(Optional.of(pixRemove));

        FailureReconciliationSyncUseCase failureReconciliationSyncUseCase = new FailureReconciliationSyncUseCase(
            bacenContentIdentifierEventsPort,
            bacenPixKeyByContentIdentifierPort,
            createPixKeyUseCase,
            removePixKeyUseCase
        );

        failureReconciliationSyncUseCase.execute(Vsync.builder().build());

        verify(createPixKeyUseCase).execute(any(), argThat(argument -> pixAdd.getKey().equals(argument.getKey())), any());
        verify(removePixKeyUseCase).execute(any(), argThat(argument -> pixRemove.getKey().equals(argument.getKey())), any());
    }

}
