package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
@ExtendWith(MockitoExtension.class)
class RequestSyncFileUseCaseTest {

    @Mock
    BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    @Mock
    DatabaseContentIdentifierPort databaseContentIdentifierPort;

    @InjectMocks
    RequestSyncFileUseCase requestSyncFileUseCase;

    @Test
    void requestFileInBacen(){
        final var contentIdentifierFile = ContentIdentifierFile.builder()
            .url("url")
            .keyType(KeyType.CPF)
            .requestTime(LocalDateTime.now())
            .status(ContentIdentifierFile.StatusContentIdentifierFile.REQUESTED)
        .build();

        when(bacenContentIdentifierEventsPort.requestContentIdentifierFile(any())).thenReturn(contentIdentifierFile);
        //doNothing().when(databaseContentIdentifierPort).save(any());

        this.requestSyncFileUseCase.execute(KeyType.CPF);

        verify(bacenContentIdentifierEventsPort).requestContentIdentifierFile(any());
        //verify(databaseContentIdentifierPort).save(any());

    }
}
