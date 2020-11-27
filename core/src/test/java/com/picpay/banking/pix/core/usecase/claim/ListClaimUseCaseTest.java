package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.ListPendingClaimPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.picpay.banking.pix.core.domain.ClaimSituation.OPEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListClaimUseCaseTest {

    @InjectMocks
    private ListClaimUseCase useCase;

    @Mock
    private ListClaimPort listClaimPort;

    @Mock
    private ListPendingClaimPort listPendingClaimPort;

    private Claim claimRequest;

    private ClaimIterable claimIterablePending;

    private ClaimIterable claimIterableIsDonor;

    private ClaimIterable claimIterableIsClaimer;

    @BeforeEach
    void setup() {
        claimRequest = Claim.builder()
                .ispb(22896431)
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("11122233300")
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .build();

        var claimResponse = Claim.builder()
                .ispb(22896431)
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("11122233300")
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .claimSituation(OPEN)
                .build();

        claimIterablePending = ClaimIterable.builder()
                .count(1)
                .hasNext(false)
                .claims(List.of(claimResponse))
                .build();

        claimResponse.setClaimSituation(ClaimSituation.CANCELED);
        claimResponse.setDonorIspb(22896431);

        claimIterableIsDonor = ClaimIterable.builder()
                .count(1)
                .hasNext(false)
                .claims(List.of(claimResponse))
                .build();

        claimResponse.setIsClaim(true);
        claimResponse.setDonorIspb(0);

        claimIterableIsClaimer = ClaimIterable.builder()
                .count(1)
                .hasNext(false)
                .claims(List.of(claimResponse))
                .build();
    }

    @Test
    void when_listPendentClaimsWithSuccess_expect_claimIterable() {
        when(listPendingClaimPort.list(any(), anyInt(), anyString())).thenReturn(claimIterablePending);

        ClaimIterable claimList = useCase.execute(claimRequest, true, 5, true, false, LocalDateTime.now(), null, "12345");

        assertEquals(1, claimList.getCount());

        verify(listPendingClaimPort, times(1)).list(any(), anyInt(), anyString());
    }

    @Test
    void when_listClaimsWhereIsDonorWithSuccess_expect_claimIterable() {
        when(listClaimPort.list(any(), anyInt(), any(), any(), any(), any(), anyString())).thenReturn(claimIterableIsDonor);

        ClaimIterable claimList = useCase.execute(claimRequest, false, 5, null, true, LocalDateTime.now(), null, "12345");

        assertEquals(1, claimList.getCount());

        verify(listClaimPort, times(1)).list(any(), anyInt(), any(), any(), any(), any(), anyString());
    }

    @Test
    void when_listClaimsWhereIsClaimerWithSuccess_expect_claimIterable() {
        when(listClaimPort.list(any(), anyInt(), any(), any(), any(), any(), anyString())).thenReturn(claimIterableIsClaimer);

        ClaimIterable claimList = useCase.execute(claimRequest, false, 5, true, null, LocalDateTime.now(), null, "12345");

        assertEquals(1, claimList.getCount());

        verify(listClaimPort, times(1)).list(any(), anyInt(), any(), any(), any(), any(), anyString());
    }

    @Test
    void when_listClaimsWithoutIsClaimerAndWithoutIsDonor_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(claimRequest, false, 5, null, null, LocalDateTime.now(), null, "12345"));

        verify(listClaimPort, times(0)).list(any(), anyInt(), any(), any(), any(), any(), anyString());
    }

    @Test
    void when_listClaimsWithIsClaimerAndWithIsDonor_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(claimRequest, false, 5, true, true, LocalDateTime.now(), null, "12345"));

        verify(listClaimPort, times(0)).list(any(), anyInt(), any(), any(), any(), any(), anyString());
    }

}