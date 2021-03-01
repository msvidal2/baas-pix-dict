package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.ResultCidFile;
import com.picpay.banking.pix.core.exception.CidFileNotReadyException;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.PollCidFilePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
class RequestSyncFileUseCaseTest {

    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private DatabaseContentIdentifierPort databaseContentIdentifierPort;
    private PollCidFilePort pollCidFilePort;

    private RequestSyncFileUseCase requestSyncFileUseCase;

    @BeforeEach
    void init() {
        bacenContentIdentifierEventsPort = Mockito.mock(BacenContentIdentifierEventsPort.class);
        databaseContentIdentifierPort = Mockito.mock(DatabaseContentIdentifierPort.class);
        pollCidFilePort = Mockito.mock(PollCidFilePort.class);
        requestSyncFileUseCase = new RequestSyncFileUseCase(bacenContentIdentifierEventsPort, databaseContentIdentifierPort, pollCidFilePort, 1, 10);
    }

    @Test
    void when_file_is_requested_from_bacen_then_wait_for_it() {
        final var requestedFile = ContentIdentifierFile.builder()
            .url("url")
            .keyType(KeyType.CPF)
            .requestTime(LocalDateTime.now())
            .status(ContentIdentifierFile.StatusContentIdentifierFile.REQUESTED)
            .build();

        when(bacenContentIdentifierEventsPort.requestContentIdentifierFile(any())).thenReturn(requestedFile);
        doNothing().when(databaseContentIdentifierPort).saveFile(any());
        when(pollCidFilePort.poll(any(), anyInt(), any(), anyInt(), any())).thenReturn(Optional.of(ResultCidFile.emptyCidFile()));

        var receivedFile = requestSyncFileUseCase.requestAwaitFile(KeyType.CPF);

        assertThat(receivedFile).isNotNull();
        verify(databaseContentIdentifierPort).saveFile(any());
    }

    @Test
    void when_file_cannot_be_obtained_then_raise_error() {
        final var requestedFile = ContentIdentifierFile.builder()
            .url("url")
            .keyType(KeyType.CPF)
            .requestTime(LocalDateTime.now())
            .status(ContentIdentifierFile.StatusContentIdentifierFile.REQUESTED)
            .id(123)
            .build();

        when(bacenContentIdentifierEventsPort.requestContentIdentifierFile(any())).thenReturn(requestedFile);
        doNothing().when(databaseContentIdentifierPort).saveFile(any());
        when(pollCidFilePort.poll(any(), anyInt(), any(), anyInt(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requestSyncFileUseCase.requestAwaitFile(KeyType.CPF))
            .isInstanceOf(CidFileNotReadyException.class)
            .hasMessageContaining("O arquivo de CIDs 123 não está disponível no BACEN");

        verify(databaseContentIdentifierPort).saveFile(any());
    }

}
