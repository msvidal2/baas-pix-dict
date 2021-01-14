package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyAutomaticallyPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OverduePossessionClaimDonorUseCaseTest {

    private static final Integer PICPAY_ISPB = 22896431;

    private static final LocalDateTime NOW = LocalDateTime.now();

    @InjectMocks
    private OverduePossessionClaimDonorUseCase useCase;

    @Mock
    private ConfirmClaimPort confirmClaimPort;

    @Mock
    private CreateClaimPort saveClaimPort;

    @Mock
    private RemovePixKeyAutomaticallyPort removePixKeyAutomaticallyPort;

    private Claim claimRequest;

    private Claim claimResponse;

    @BeforeEach
    public void setup() {
        claimRequest = Claim.builder()
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
                .donorIspb(PICPAY_ISPB)
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .completionThresholdDate(NOW.plusDays(7))
                .resolutionThresholdDate(NOW.plusDays(7))
                .lastModifiedDate(NOW)
                .confirmationReason(ClaimConfirmationReason.CLIENT_REQUEST)
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .build();

        claimResponse = Claim.builder()
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
                .donorIspb(PICPAY_ISPB)
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .completionThresholdDate(NOW.plusDays(7))
                .resolutionThresholdDate(NOW.plusDays(7))
                .lastModifiedDate(NOW)
                .confirmationReason(ClaimConfirmationReason.CLIENT_REQUEST)
                .claimSituation(ClaimSituation.CONFIRMED)
                .build();
    }

    //@Test
    void when_executeWithBacenConfirmation_expect_saveClaimDeleteKey() {
        when(confirmClaimPort.confirm(any(), any(), isNull())).thenReturn(claimResponse);
        when(saveClaimPort.saveClaim(any(), isNull())).thenReturn(claimResponse);
        doNothing().when(removePixKeyAutomaticallyPort).remove(anyString(), any());

        assertDoesNotThrow(() -> useCase.confirm(claimRequest, null));

        verify(confirmClaimPort, times(1)).confirm(any(), any(), isNull());
        verify(saveClaimPort, times(1)).saveClaim(any(), isNull());
        verify(removePixKeyAutomaticallyPort, times(1)).remove(anyString(), any());
    }

    //@Test
    void when_executeWithBacenFail_expect_exception() {
        when(confirmClaimPort.confirm(any(), any(), anyString())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> useCase.confirm(claimRequest, null));

        verify(confirmClaimPort, times(1)).confirm(any(), any(), isNull());
        verify(saveClaimPort, times(0)).saveClaim(any(), isNull());
        verify(removePixKeyAutomaticallyPort, times(0)).remove(anyString(), any());
    }



}
