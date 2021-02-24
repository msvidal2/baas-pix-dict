/*
 *  baas-pix-dict 1.0 19/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.ResultCidFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchCidFileCallablePortTest {

    @Mock
    private BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private FetchCidFileCallablePort fetchCidFileCallablePort;


    @Test
    void when_file_is_unavailable_then_return_empty_file() {
        fetchCidFileCallablePort = new FetchCidFileCallablePort(bacenContentIdentifierEventsPort, 123);

        when(bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(123)).thenReturn(null);

        Optional<ResultCidFile> call = fetchCidFileCallablePort.call();
        assertThat(call).isEmpty();
    }

    @Test
    void when_file_is_available_then_return_file() {
        fetchCidFileCallablePort = new FetchCidFileCallablePort(bacenContentIdentifierEventsPort, 123);

        when(bacenContentIdentifierEventsPort.getContentIdentifierFileInBacen(123)).thenReturn(getContentIdentifierFile());

        Optional<ResultCidFile> call = fetchCidFileCallablePort.call();
        assertThat(call).isNotEmpty();
    }

    private ContentIdentifierFile getContentIdentifierFile() {
        return ContentIdentifierFile.builder()
            .id(123)
            .status(ContentIdentifierFile.StatusContentIdentifierFile.AVAILABLE)
            .build();
    }

}