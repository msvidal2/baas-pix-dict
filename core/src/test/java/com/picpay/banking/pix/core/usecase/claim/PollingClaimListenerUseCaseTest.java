package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.claim.bacen.AcknowledgeClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendClaimNotificationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PollingClaimListenerUseCaseTest {

    private static final Integer PICPAY_ISPB = 22896431;

    private static final LocalDateTime NOW = LocalDateTime.now();

    @Mock
    private AcknowledgeClaimPort acknowledgeClaimPort;

    @Mock
    private CreateClaimPort saveClaimPort;

    @Mock
    private SendClaimNotificationPort sendClaimNotificationPort;

    private PollingClaimListenerUseCase useCase;

    private Claim claimRequest;

    private Claim claimResponse;

    @BeforeEach
    public void setup() {
        useCase = new PollingClaimListenerUseCase(
                PICPAY_ISPB,
                acknowledgeClaimPort,
                saveClaimPort,
                sendClaimNotificationPort);

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
                .claimSituation(ClaimSituation.OPEN)
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
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .build();
    }

    @Test
    void when_claimIsOpenForParticipant_expect_bacenAcknowledge() {
        when(acknowledgeClaimPort.acknowledge(anyString(), anyInt())).thenReturn(claimResponse);
        when(saveClaimPort.saveClaim(any(), isNull())).thenReturn(claimResponse);
        doNothing().when(sendClaimNotificationPort).send(any());

        useCase.execute(claimRequest);

        verify(acknowledgeClaimPort, times(1)).acknowledge(claimRequest.getClaimId(), PICPAY_ISPB);
        verify(saveClaimPort, times(1)).saveClaim(any(), isNull());
        verify(sendClaimNotificationPort, times(1)).send(any());
    }

    @Test
    void when_claimIsNotOpenForParticipant_expect_saveAndSend() {
        claimRequest.setClaimSituation(ClaimSituation.COMPLETED);

        when(saveClaimPort.saveClaim(any(), isNull())).thenReturn(claimResponse);
        doNothing().when(sendClaimNotificationPort).send(any());

        useCase.execute(claimRequest);

        verify(acknowledgeClaimPort, times(0)).acknowledge(claimRequest.getClaimId(), PICPAY_ISPB);
        verify(saveClaimPort, times(1)).saveClaim(any(), isNull());
        verify(sendClaimNotificationPort, times(1)).send(any());
    }

    @Test
    void when_claimIsOpenForDifferentParticipant_expect_saveAndSend() {
        claimRequest.setDonorIspb(12345678);

        when(saveClaimPort.saveClaim(any(), isNull())).thenReturn(claimResponse);
        doNothing().when(sendClaimNotificationPort).send(any());

        useCase.execute(claimRequest);

        verify(acknowledgeClaimPort, times(0)).acknowledge(claimRequest.getClaimId(), PICPAY_ISPB);
        verify(saveClaimPort, times(1)).saveClaim(any(), isNull());
        verify(sendClaimNotificationPort, times(1)).send(any());
    }

}
