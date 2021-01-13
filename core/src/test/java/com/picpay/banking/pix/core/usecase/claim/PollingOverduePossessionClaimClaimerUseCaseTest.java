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
public class PollingOverduePossessionClaimClaimerUseCaseTest {

    private static final Integer PICPAY_ISPB = 22896431;

    private static final Integer LIMIT = 200;

    @InjectMocks
    private PollingOverduePossessionClaimClaimerUseCase useCase;

    @Mock
    private FindClaimToCancelPort findClaimToCancelPort;

    @Mock
    private SendOverduePossessionClaimPort sendOverduePossessionClaimPort;

    @Test
    void when_findClaimWhereIsClaimerPortReturnElements_expect_numElementsSendCalls() {
        when(findClaimToCancelPort.findClaimToCancelWhereIsClaimer(any(), anyList(), anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(new Claim(), new Claim(), new Claim()));

        assertDoesNotThrow(() -> useCase.executeForClaimer(PICPAY_ISPB, LIMIT));

        verify(sendOverduePossessionClaimPort, times(3)).sendToCancel(any());
    }

    @Test
    void when_findClaimWhereIsDonorPortReturnNull_expect_nothingHappens() {
        when(findClaimToCancelPort.findClaimToCancelWhereIsClaimer(any(), anyList(), anyInt(), anyInt(), anyInt()))
                .thenReturn(null);

        assertDoesNotThrow(() -> useCase.executeForClaimer(PICPAY_ISPB, LIMIT));

        verify(sendOverduePossessionClaimPort, times(0)).sendToCancel(any());
    }

    @Test
    void when_findClaimWhereIsClaimerPortReturnEmpty_expect_nothingHappens() {
        when(findClaimToCancelPort.findClaimToCancelWhereIsClaimer(any(), anyList(), anyInt(), anyInt(), anyInt()))
                .thenReturn(Lists.newArrayList());

        assertDoesNotThrow(() -> useCase.executeForClaimer(PICPAY_ISPB, LIMIT));

        verify(sendOverduePossessionClaimPort, times(0)).sendToCancel(any());
    }

}
