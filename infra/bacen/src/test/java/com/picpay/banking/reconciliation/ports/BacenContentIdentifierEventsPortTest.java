package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.BacenContentIdentifierEventsPort;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.reconciliation.clients.BacenArqClient;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import com.picpay.banking.reconciliation.dto.response.CidSetFile;
import com.picpay.banking.reconciliation.dto.response.CidSetFileResponse;
import com.picpay.banking.reconciliation.dto.response.GetCidSetFileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Luis Silva
 * @version 1.0 08/12/2020
 */
@ExtendWith(MockitoExtension.class)
public class BacenContentIdentifierEventsPortTest {

    @Mock
    private BacenArqClient bacenArqClient;
    @Mock
    private BacenReconciliationClient bacenReconciliationClient;

    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private CidSetFile cidSetFile;

    @BeforeEach
    public void init(){
        this.bacenContentIdentifierEventsPort =
            new BacenContentIdentifierEventsPortImpl(bacenArqClient,bacenReconciliationClient,"22896431", "http://urlGateway.com");

        this.cidSetFile = CidSetFile.builder()
            .id(1)
            .status(ContentIdentifierFile.StatusContentIdentifierFile.AVAILABLE)
            .participant(22896431)
            .url("http://localhost.com")
            .sha256("")
            .keyType(KeyTypeBacen.CPF)
            .build();
    }

    @Test
    public void shouldRequestCIDFileInBacen(){

        final var cidSetFileResponse = CidSetFileResponse.builder().cidSetFile(cidSetFile).build();
        when(this.bacenReconciliationClient.requestCidFile(any())).thenReturn(cidSetFileResponse);

        final var contentIdentifierFile = this.bacenContentIdentifierEventsPort.requestContentIdentifierFile(KeyType.CPF);
        assertThat(contentIdentifierFile.getStatus()).isEqualTo(ContentIdentifierFile.StatusContentIdentifierFile.AVAILABLE);

        verify(this.bacenReconciliationClient).requestCidFile(any());
    }

    @Test
    public void shouldGetCidFileInBacen(){

        when(this.bacenReconciliationClient.getCidFile(any(),anyString()))
            .thenReturn(GetCidSetFileResponse.builder().cidSetFile(cidSetFile).build());

        this.bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(1);

        verify(this.bacenReconciliationClient).getCidFile(any(),anyString());

    }

    @Test
    public void shouldDownloadFileInBacenIgnoringEmptyLines(){

        var cids = "ae843d282551398d7d201be38cb2f6472cfed56aa8a1234612780f9618ec017a\n" +
            "b42a52ec1ce47529d9cade36c9a4b6d89c512ab0660662b4b9a0949055d7c935\n" +
            " ";

        when(this.bacenArqClient.request(any()))
            .thenReturn(cids);

        final var listCids = this.bacenContentIdentifierEventsPort.downloadCidsFromBacen("http://teste.com/teste/arquivo");
        assertThat(listCids.size()).isEqualTo(2);

        verify(this.bacenArqClient).request(argThat(uri -> uri.getHost().equals("urlGateway.com") && uri.getPath().startsWith("/arq/")));

    }

}
