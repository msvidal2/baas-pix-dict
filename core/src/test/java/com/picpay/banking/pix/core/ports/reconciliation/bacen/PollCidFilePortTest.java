/*
 *  baas-pix-dict 1.0 19/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.ResultCidFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author rafael.braga
 * @version 1.0 19/02/2021
 */
@ExtendWith(MockitoExtension.class)
class PollCidFilePortTest {

    @Mock
    private FetchCidFileCallablePort fetchCidFileCallablePort;

    @Test
    void when_polling_finds_file_then_return_its_content() {
        when(fetchCidFileCallablePort.call()).thenReturn(Optional.of(ResultCidFile.emptyCidFile()));

        new PollCidFilePort().poll(fetchCidFileCallablePort, 1, TimeUnit.HOURS, 10, TimeUnit.SECONDS);

        verify(fetchCidFileCallablePort).call();
    }

    @Test
    void when_polling_fetchs_files_and_retries_then_return_file() {
        doAnswer(new Answer<>() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count++ == 0) {
                    return Optional.empty();
                }

                return Optional.of(ResultCidFile.emptyCidFile());
            }
        }).when(fetchCidFileCallablePort).call();

        new PollCidFilePort().poll(fetchCidFileCallablePort, 5, TimeUnit.SECONDS, 1, TimeUnit.HOURS);

        verify(fetchCidFileCallablePort, times(2)).call();
    }

}