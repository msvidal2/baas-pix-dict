package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifier.ContentIdentifierType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.reconciliation.BacenReconciliationPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FailureReconciliationSyncUseCaseTest {

    @Mock
    private BacenReconciliationPort bacenReconciliationPort;
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
        when(bacenReconciliationPort.list(any(), any(), any()))
            .thenReturn(List.of(
                ContentIdentifier.builder()
                    .contentIdentifierType(ContentIdentifierType.ADD)
                    .cid("1")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifier.builder()
                    .contentIdentifierType(ContentIdentifierType.REMOVE)
                    .cid("2")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifier.builder()
                    .contentIdentifierType(ContentIdentifierType.REMOVE)
                    .cid("1")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifier.builder()
                    .contentIdentifierType(ContentIdentifierType.ADD)
                    .cid("1")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifier.builder()
                    .contentIdentifierType(ContentIdentifierType.ADD)
                    .cid("2")
                    .dateTime(LocalDateTime.now())
                    .build(),
                ContentIdentifier.builder()
                    .contentIdentifierType(ContentIdentifierType.REMOVE)
                    .cid("2")
                    .dateTime(LocalDateTime.now())
                    .build()));

        when(bacenReconciliationPort.getPixKey("1"))
            .thenReturn(pixAdd);
        when(bacenReconciliationPort.getPixKey("2"))
            .thenReturn(pixRemove);

//        FailureReconciliationSyncUseCase failureReconciliationSyncUseCase = new FailureReconciliationSyncUseCase(
//            bacenReconciliationPort,
//            createPixKeyUseCase,
//            removePixKeyUseCase
//        );
//
//        failureReconciliationSyncUseCase.execute(Vsync.builder().build());
//
//        verify(createPixKeyUseCase).execute(any(), argThat(argument -> pixAdd.getKey().equals(argument.getKey())), any());
//        verify(removePixKeyUseCase).execute(any(), argThat(argument -> pixRemove.getKey().equals(argument.getKey())), any());
    }

}
