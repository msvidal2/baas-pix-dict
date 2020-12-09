package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.claim.FindClaimLastPollingDatePort;
import com.picpay.banking.pix.core.ports.claim.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.claim.UpdateClaimLastPollingDatePort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PollingClaimUseCaseTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    @InjectMocks
    private PollingClaimUseCase useCase;

    @Mock
    private ListClaimsBacenPort listClaimsBacenPort;

    @Mock
    private SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort;

    @Mock
    private FindClaimLastPollingDatePort findClaimLastPollingDatePort;

    @Mock
    private UpdateClaimLastPollingDatePort updateClaimLastPollingDatePort;

    @Test
    void when_executeWithSuccess_expect_noExceptions() {
        var claimIterableMock = ClaimIterable.builder()
                .hasNext(false)
                .claims(List.of(Claim.builder()
                        .claimType(ClaimType.POSSESSION_CLAIM)
                        .key("+5561988887777")
                        .keyType(KeyType.CELLPHONE)
                        .ispb(12345678)
                        .branchNumber("0001")
                        .accountNumber("0007654321")
                        .accountType(AccountType.CHECKING)
                        .accountOpeningDate(NOW)
                        .personType(PersonType.INDIVIDUAL_PERSON)
                        .cpfCnpj("11122233300")
                        .name("João Silva")
                        .donorIspb(87654321)
                        .claimId("123e4567-e89b-12d3-a456-426655440000")
                        .completionThresholdDate(NOW.plusDays(7))
                        .resolutionThresholdDate(NOW.plusDays(7))
                        .lastModifiedDate(NOW)
                        .build()))
                .build();

        when(listClaimsBacenPort.list(any(), anyInt(), nullable(Boolean.class), any(), any()))
                .thenReturn(claimIterableMock);

        assertDoesNotThrow(() -> useCase.execute(123456, 200));
    }

    @Test
    void when_executeWithClaimListNull_expect_noExceptions() {
        var claimIterableMock = ClaimIterable.builder()
                .hasNext(false)
                .build();

        when(listClaimsBacenPort.list(any(), anyInt(), nullable(Boolean.class), any(), any()))
                .thenReturn(claimIterableMock);

        assertDoesNotThrow(() -> useCase.execute(123456, 200));
    }

    @Test
    void when_executeWithClaimListEmpty_expect_noExceptions() {
        var claimIterableMock = ClaimIterable.builder()
                .hasNext(false)
                .claims(Collections.emptyList())
                .build();

        when(listClaimsBacenPort.list(any(), anyInt(), nullable(Boolean.class), any(), any()))
                .thenReturn(claimIterableMock);

        assertDoesNotThrow(() -> useCase.execute(123456, 200));
    }

}