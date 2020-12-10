package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
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
public class RequestSyncFileUseCaseTest {

    @Mock
    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    @Mock
    private DatabaseContentIdentifierPort databaseContentIdentifierPort;

    @InjectMocks
    private RequestSyncFileUseCase requestSyncFileUseCase;

    @Test
    public void requestFileInBacen(){
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
