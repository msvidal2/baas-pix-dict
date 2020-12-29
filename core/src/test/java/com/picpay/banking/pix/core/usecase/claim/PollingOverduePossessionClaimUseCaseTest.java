package com.picpay.banking.pix.core.usecase.claim;

import com.google.common.collect.Lists;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendOverduePossessionClaimPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PollingOverduePossessionClaimUseCaseTest {

    private static final Integer PICPAY_ISPB = 22896431;

    private static final Integer LIMIT = 200;

    @InjectMocks
    private PollingOverduePossessionClaimUseCase useCase;

    @Mock
    private FindClaimToCancelPort findClaimToCancelPort;

    @Mock
    private SendOverduePossessionClaimPort sendOverduePossessionClaimPort;

    @Test
    void when_findClaimPortReturnElements_expect_numElementsSendCalls() {
        when(findClaimToCancelPort.find(any(), anyList(), anyInt(), any(), anyInt()))
                .thenReturn(List.of(new Claim(), new Claim(), new Claim()));

        assertDoesNotThrow(() -> useCase.execute(PICPAY_ISPB, LIMIT));

        verify(sendOverduePossessionClaimPort, times(3)).send(any());
    }

    @Test
    void when_findClaimPortReturnNull_expect_nothingHappens() {
        when(findClaimToCancelPort.find(any(), anyList(), anyInt(), any(), anyInt()))
                .thenReturn(null);

        assertDoesNotThrow(() -> useCase.execute(PICPAY_ISPB, LIMIT));

        verify(sendOverduePossessionClaimPort, times(0)).send(any());
    }

    @Test
    void when_findClaimPortReturnEmpty_expect_nothingHappens() {
        when(findClaimToCancelPort.find(any(), anyList(), anyInt(), any(), anyInt()))
                .thenReturn(Lists.newArrayList());

        assertDoesNotThrow(() -> useCase.execute(PICPAY_ISPB, LIMIT));

        verify(sendOverduePossessionClaimPort, times(0)).send(any());
    }

}
