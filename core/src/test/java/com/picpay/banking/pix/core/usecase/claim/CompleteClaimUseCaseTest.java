package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.picpay.banking.pix.core.domain.ClaimSituation.OPEN;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteClaimUseCaseTest {

    @InjectMocks
    private CompleteClaimUseCase useCase;

    @Mock
    private CompleteClaimBacenPort completeClaimBacenPort;

    @Mock
    private CompleteClaimPort completeClaimPort;

    @Mock
    private FindClaimPort findClaimPort;

    @Mock
    private SavePixKeyPort createPixKeyPort;

    @Mock
    private PixKeyEventPort pixKeyEventPort;

    private Claim claimRequest;

    private Claim claimResponseConfirmed;

    private Claim claimResponseCompleted;

    private Claim claimResponseOpened;

    private Claim possessionClaim;

    private PixKey pixKeyCreated;


    @BeforeEach
    public void setup() {
        claimRequest = Claim.builder()
                .ispb(22896431)
                .claimId(randomUUID().toString())
                .build();

        claimResponseConfirmed = Claim.builder()
                .ispb(22896431)
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .claimSituation(ClaimSituation.CONFIRMED)
                .build();

        claimResponseCompleted = Claim.builder()
                .ispb(22896431)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .claimSituation(ClaimSituation.CONFIRMED)
                .build();

        claimResponseOpened = Claim.builder()
                .ispb(22896431)
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .claimSituation(OPEN)
                .build();

        possessionClaim = Claim.builder()
                .ispb(22896431)
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .claimSituation(OPEN)
                .claimType(ClaimType.POSSESSION_CLAIM)
                .completionThresholdDate(LocalDateTime.of(2020, 12, 30, 19, 30))
                .build();

        pixKeyCreated = PixKey.builder()
                .type(KeyType.EMAIL)
                .key("joao@picpay.com")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .endToEndId("endToEndId").build();

        useCase =  new CompleteClaimUseCase(completeClaimBacenPort, completeClaimPort, findClaimPort, createPixKeyPort, pixKeyEventPort);
    }

    @Test
    void when_completeClaimWithSuccess_expect_claim() {
        when(findClaimPort.findClaim(anyString(), anyInt(), anyBoolean())).thenReturn(Optional.of(claimResponseConfirmed));
        when(completeClaimBacenPort.complete(any(), anyString())).thenReturn(claimResponseCompleted);
        when(completeClaimPort.complete(any(), anyString())).thenReturn(claimResponseCompleted);
        when(createPixKeyPort.savePixKey(any(), any())).thenReturn(pixKeyCreated);

        Claim claimResult = useCase.execute(claimRequest, randomUUID().toString());

        assertEquals(claimResult.getClaimId(), claimResponseCompleted.getClaimId());

        verify(findClaimPort, times(1)).findClaim(anyString(), anyInt(), anyBoolean());
        verify(completeClaimBacenPort, times(1)).complete(any(), anyString());
        verify(completeClaimPort, times(1)).complete(any(), anyString());
        verify(createPixKeyPort, times(1)).savePixKey(any(), any());
    }

    @Test
    void when_completeClaimWithStatusNotConfirmed_expect_claimException() {
        when(findClaimPort.findClaim(anyString(), anyInt(), anyBoolean())).thenReturn(Optional.of(claimResponseOpened));

        assertThrows(ClaimException.class, () -> useCase.execute(claimRequest, randomUUID().toString()));

        verify(findClaimPort, times(1)).findClaim(anyString(), anyInt(), anyBoolean());
        verify(completeClaimBacenPort, times(0)).complete(any(), anyString());
        verify(completeClaimPort, times(0)).complete(any(), anyString());
        verify(createPixKeyPort, times(0)).savePixKey(any(), any());
    }

    @Test
    void when_completeClaimPossessionWithInvalidCompletionThresholdDate_expect_claimException() {
        when(findClaimPort.findClaim(anyString(), anyInt(), anyBoolean())).thenReturn(Optional.of(possessionClaim));

        assertThrows(ClaimException.class, () -> useCase.execute(claimRequest, randomUUID().toString()));

        verify(findClaimPort, times(1)).findClaim(anyString(), anyInt(), anyBoolean());
        verify(completeClaimBacenPort, times(0)).complete(any(), anyString());
        verify(completeClaimPort, times(0)).complete(any(), anyString());
        verify(createPixKeyPort, times(0)).savePixKey(any(), any());
    }

    @Test
    void testCompleteNullClaimId() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                                .claimId(null)
                                .ispb(23542432)
                                .build(),
                        randomUUID().toString()));
    }

    @Test
    void testCompleteEmptyClaimId() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                                .claimId("")
                                .ispb(23542432)
                                .build(),
                        randomUUID().toString()));
    }

    @Test
    void testCompleteInvalidClaimId() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                                .claimId("adncyt3874yt837y")
                                .ispb(23542432)
                                .build(),
                        randomUUID().toString()));
    }

    @Test
    void testCompleteNullRequestIdentifier() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                                .claimId(randomUUID().toString())
                                .ispb(23423)
                                .build(),
                        null));
    }

    @Test
    void testCompleteEmptyRequestIdentifier() {
        assertThrows(IllegalArgumentException .class, () ->
                useCase.execute(Claim.builder()
                                .claimId(randomUUID().toString())
                                .ispb(-42432)
                                .build(),
                        ""));
    }

    @Test
    void testCompleteInvalidIspb() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                                .claimId(randomUUID().toString())
                                .ispb(0)
                                .build(),
                        randomUUID().toString()));
    }

    @Test
    void testCompleteNegativeIspb() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                                .claimId(randomUUID().toString())
                                .ispb(-42432)
                                .build(),
                        randomUUID().toString()));
    }
}