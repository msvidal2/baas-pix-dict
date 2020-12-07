package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfirmClaimUseCaseTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private static final String RANDOM_UUID = UUID.randomUUID().toString();

    @InjectMocks
    private ConfirmClaimUseCase useCase;

    @Mock
    private ConfirmClaimPort confirmClaimPort;

    @Mock
    private FindClaimPort findClaimPort;

    @Mock
    private CreateClaimPort saveClaimPort;

    @Mock
    private RemovePixKeyPort removePixKeyPort;

    private Claim claim;

    @BeforeEach
    public void setup() {
        claim = Claim.builder()
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
                .confirmationReason(ClaimConfirmationReason.CLIENT_REQUEST)
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .build();
    }

    @Test
    void when_confirmClaimWithSuccess_expect_confirmedSituation() {
        when(findClaimPort.findClaim(anyString(), anyInt(), anyBoolean())).thenReturn(Optional.of(claim));
        when(confirmClaimPort.confirm(any(), any(), anyString())).thenReturn(claim);
        when(saveClaimPort.saveClaim(any(), anyString())).thenReturn(claim);
        when(removePixKeyPort.remove(any(), anyInt())).thenReturn(null);

        Claim claimConfirmed = useCase.execute(claim, ClaimConfirmationReason.CLIENT_REQUEST, UUID.randomUUID().toString());

        assertEquals(claim.getClaimId(), claimConfirmed.getClaimId());
        assertEquals(claim.getClaimSituation(), claimConfirmed.getClaimSituation());

        verify(confirmClaimPort, times(1)).confirm(any(), any(), anyString());
        verify(findClaimPort, times(1)).findClaim(anyString(), anyInt(), anyBoolean());
        verify(saveClaimPort, times(1)).saveClaim(any(), anyString());
        verify(removePixKeyPort, times(1)).remove(any(), anyInt());
    }

    @Test
    void when_confirmClaimWithInvalidClaimSituation_expect_claimException() {
        claim.setClaimSituation(ClaimSituation.CONFIRMED);

        when(findClaimPort.findClaim(anyString(), anyInt(), anyBoolean())).thenReturn(Optional.of(claim));

        assertThrows(ClaimException.class, () -> useCase.execute(claim, ClaimConfirmationReason.CLIENT_REQUEST, RANDOM_UUID));

        verify(confirmClaimPort, times(0)).confirm(any(), any(), anyString());
        verify(findClaimPort, times(1)).findClaim(anyString(), anyInt(), anyBoolean());
        verify(saveClaimPort, times(0)).saveClaim(any(), anyString());
        verify(removePixKeyPort, times(0)).remove(any(), anyInt());
    }

    @Test
    void when_confirmPortabilityClaimWithInvalidClaimReason_expect_claimException() {
        claim.setClaimType(ClaimType.PORTABILITY);

        when(findClaimPort.findClaim(anyString(), anyInt(), anyBoolean())).thenReturn(Optional.of(claim));

        assertThrows(ClaimException.class, () -> useCase.execute(claim, ClaimConfirmationReason.DEFAULT_RESPONSE, RANDOM_UUID));

        verify(confirmClaimPort, times(0)).confirm(any(), any(), anyString());
        verify(findClaimPort, times(1)).findClaim(anyString(), anyInt(), anyBoolean());
        verify(saveClaimPort, times(0)).saveClaim(any(), anyString());
        verify(removePixKeyPort, times(0)).remove(any(), anyInt());
    }

    @Test
    void when_confirmPossessionClaimWithInvalidClaimReason_expect_claimException() {
        claim.setClaimType(ClaimType.POSSESSION_CLAIM);

        when(findClaimPort.findClaim(anyString(), anyInt(), anyBoolean())).thenReturn(Optional.of(claim));

        assertThrows(ClaimException.class, () -> useCase.execute(claim, ClaimConfirmationReason.ACCOUNT_CLOSURE, RANDOM_UUID));

        verify(confirmClaimPort, times(0)).confirm(any(), any(), anyString());
        verify(findClaimPort, times(1)).findClaim(anyString(), anyInt(), anyBoolean());
        verify(saveClaimPort, times(0)).saveClaim(any(), anyString());
        verify(removePixKeyPort, times(0)).remove(any(), anyInt());
    }

    @Test
    void when_confirmClaimWithInvalidResolutionPeriod_expect_claimException() {
        claim.setClaimType(ClaimType.POSSESSION_CLAIM);
        claim.setResolutionThresholdDate(NOW.plusWeeks(1));

        when(findClaimPort.findClaim(anyString(), anyInt(), anyBoolean())).thenReturn(Optional.of(claim));

        assertThrows(ClaimException.class, () -> useCase.execute(claim, ClaimConfirmationReason.DEFAULT_RESPONSE, RANDOM_UUID));

        verify(confirmClaimPort, times(0)).confirm(any(), any(), anyString());
        verify(findClaimPort, times(1)).findClaim(anyString(), anyInt(), anyBoolean());
        verify(saveClaimPort, times(0)).saveClaim(any(), anyString());
        verify(removePixKeyPort, times(0)).remove(any(), anyInt());
    }


    @Test
    void testConfirmNullClaimId() {
        assertThrows(IllegalArgumentException.class, () -> {
            var claim = Claim.builder()
            .ispb(23542432).build();
        useCase.execute(claim, ClaimConfirmationReason.CLIENT_REQUEST, randomUUID().toString());});
    }

    @Test
    void testConfirmEmptyClaimId() {
        assertThrows(IllegalArgumentException.class, () -> {
            var claim = Claim.builder()
                .claimId("")
                .ispb(23542432).build();
            useCase.execute(claim, ClaimConfirmationReason.CLIENT_REQUEST, randomUUID().toString());});
    }


    @Test
    void testConfirmInvalidClaimId() {
        assertThrows(IllegalArgumentException.class, () -> {
            var claim = Claim.builder()
                .claimId("adncyt3874yt837y")
                .ispb(23542432).build();
            useCase.execute(claim, ClaimConfirmationReason.CLIENT_REQUEST, randomUUID().toString());});
    }

    @Test
    void testConfirmNullReason() {
        assertThrows(IllegalArgumentException.class, () -> {
            var claim = Claim.builder()
                .claimId(randomUUID().toString())
                .ispb(23542432).build();
            useCase.execute(claim, null, randomUUID().toString());});
    }

    @Test
    void testConfirmNullRequestIdentifier() {
        assertThrows(IllegalArgumentException.class, () -> {
            var claim = Claim.builder()
                .claimId(randomUUID().toString())
                .ispb(23542432).build();
            useCase.execute(claim, ClaimConfirmationReason.CLIENT_REQUEST, null);});
    }
}