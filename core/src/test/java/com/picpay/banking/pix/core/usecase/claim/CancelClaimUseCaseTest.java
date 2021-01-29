package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindByIdPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.picpay.banking.pix.core.domain.ClaimCancelReason.*;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelClaimUseCaseTest {

    @InjectMocks
    private CancelClaimUseCase useCase;

    @Mock
    private CancelClaimBacenPort cancelClaimBacenPort;

    @Mock
    private FindByIdPort findByIdPort;

    @Mock
    private CancelClaimPort cancelClaimPort;

    @Test
    void when_executeWithSuccess_expect_claimWithStatusCanceled() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        var claimCancelled = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.CANCELED)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .cancelReason(CLIENT_REQUEST)
                .isClaim(true)
                .correlationId("a9f13566e19f5ca51329479a5bae60c5")
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));
        when(cancelClaimBacenPort.cancel(anyString(), any(), anyInt(), anyString())).thenReturn(claimCancelled);

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        assertDoesNotThrow(() -> {
            var claimResponse = useCase.execute(claimCancel, true, CLIENT_REQUEST, randomUUID().toString());

            assertNotNull(claimResponse);
            assertEquals(claimCancelled.getClaimId(), claimResponse.getClaimId());
            assertEquals(claimCancelled.getClaimSituation(), claimResponse.getClaimSituation());
            assertEquals(claimCancelled.getPixKey().getKey(), claimResponse.getPixKey().getKey());
            assertEquals(claimCancelled.getPixKey().getType(), claimResponse.getPixKey().getType());
            assertEquals(claimCancelled.getIspb(), claimResponse.getIspb());
            assertEquals(claimCancelled.getBranchNumber(), claimResponse.getBranchNumber());
            assertEquals(claimCancelled.getAccountNumber(), claimResponse.getAccountNumber());
            assertEquals(claimCancelled.getAccountType(), claimResponse.getAccountType());
            assertEquals(claimCancelled.getAccountOpeningDate(), claimResponse.getAccountOpeningDate());
            assertEquals(claimCancelled.getPersonType(), claimResponse.getPersonType());
            assertEquals(claimCancelled.getCpfCnpj(), claimResponse.getCpfCnpj());
            assertEquals(claimCancelled.getName(), claimResponse.getName());
            assertEquals(claimCancelled.getDonorIspb(), claimResponse.getDonorIspb());
        });

        verify(findByIdPort).find(anyString());
        verify(cancelClaimBacenPort).cancel(anyString(), any(), anyInt(), anyString());
        verify(cancelClaimPort).cancel(any(), anyString());
    }

    @Test
    void when_executeWithValidationError_expect_illegalArgumentException() {
        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-42665544")
                .ispb(12345678)
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(claimCancel,
                false,
                CLIENT_REQUEST,
                randomUUID().toString()));
    }

    @Test
    void when_executeWithClaimNotExisting_expect_resourceNotFoundException() {
        when(findByIdPort.find(anyString())).thenReturn(Optional.empty());

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(claimCancel,
                false,
                CLIENT_REQUEST,
                randomUUID().toString()));
    }

    @Test
    void when_executeWithClaimantInvalidSituation_expect_claimException() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.OPEN)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        var error = assertThrows(ClaimException.class, () -> useCase.execute(claimCancel,
                true,
                CLIENT_REQUEST,
                randomUUID().toString())).getClaimError();

        assertEquals(ClaimError.CLAIMANT_CANCEL_SITUATION_NOT_ALLOWED, error);

        verify(findByIdPort).find(anyString());
    }

    @Test
    void when_executeWithDonorPossessionInvalidSituation_expect_claimException() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.OPEN)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        var error = assertThrows(ClaimException.class, () -> useCase.execute(claimCancel,
                false,
                CLIENT_REQUEST,
                randomUUID().toString())).getClaimError();

        assertEquals(ClaimError.POSSESSION_CLAIM_SITUATION_NOT_ALLOW_CANCELLATION, error);

        verify(findByIdPort).find(anyString());
    }

    @Test
    void when_executeWithDonorPortabilityInvalidSituation_expect_claimException() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.PORTABILITY)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.OPEN)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        var error = assertThrows(ClaimException.class, () -> useCase.execute(claimCancel,
                false,
                CLIENT_REQUEST,
                randomUUID().toString())).getClaimError();

        assertEquals(ClaimError.PORTABILITY_CLAIM_SITUATION_NOT_ALLOW_CANCELLATION, error);

        verify(findByIdPort).find(anyString());
    }

    @Test
    void when_executeWithClaimantPossessionInvalidReason_expect_noExceptions() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.CONFIRMED)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        var requestIdentifier = randomUUID().toString();

        assertDoesNotThrow(() -> useCase.execute(claimCancel, true, CLIENT_REQUEST, requestIdentifier));
        assertDoesNotThrow(() -> useCase.execute(claimCancel, true, ACCOUNT_CLOSURE, requestIdentifier));
        assertDoesNotThrow(() -> useCase.execute(claimCancel, true, FRAUD, requestIdentifier));
        assertDoesNotThrow(() -> useCase.execute(claimCancel, true, DEFAULT_RESPONSE, requestIdentifier));

        verify(findByIdPort, times(4)).find(anyString());
    }

    @Test
    void when_executeWithClaimantPortabilityInvalidReason_expect_claimException() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.PORTABILITY)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        var requestIdentifier = randomUUID().toString();

        assertDoesNotThrow(() -> useCase.execute(claimCancel, true, CLIENT_REQUEST, requestIdentifier));
        assertDoesNotThrow(() -> useCase.execute(claimCancel, true, ACCOUNT_CLOSURE, requestIdentifier));
        assertDoesNotThrow(() -> useCase.execute(claimCancel, true, FRAUD, requestIdentifier));
        assertThrows(ClaimException.class, () -> useCase.execute(claimCancel, true, DEFAULT_RESPONSE, requestIdentifier));

        verify(findByIdPort, times(4)).find(anyString());
    }

    @Test
    void when_executeWithDonorPossessionInvalidReason_expect_claimException() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        var requestIdentifier = randomUUID().toString();

        assertThrows(ClaimException.class, () -> useCase.execute(claimCancel, false, CLIENT_REQUEST, requestIdentifier));
        assertThrows(ClaimException.class, () -> useCase.execute(claimCancel, false, ACCOUNT_CLOSURE, requestIdentifier));
        assertDoesNotThrow(() -> useCase.execute(claimCancel, false, FRAUD, requestIdentifier));
        assertThrows(ClaimException.class, () -> useCase.execute(claimCancel, false, DEFAULT_RESPONSE, requestIdentifier));

        verify(findByIdPort, times(4)).find(anyString());
    }

    @Test
    void when_executeWithDonorPortabilityInvalidReason_expect_claimException() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.PORTABILITY)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        var requestIdentifier = randomUUID().toString();

        assertDoesNotThrow(() -> useCase.execute(claimCancel, false, CLIENT_REQUEST, requestIdentifier));
        assertThrows(ClaimException.class, () -> useCase.execute(claimCancel, false, ACCOUNT_CLOSURE, requestIdentifier));
        assertDoesNotThrow(() -> useCase.execute(claimCancel, false, FRAUD, requestIdentifier));
        assertDoesNotThrow(() -> useCase.execute(claimCancel, false, DEFAULT_RESPONSE, requestIdentifier));

        verify(findByIdPort, times(4)).find(anyString());
    }

    @Test
    void when_executeWithClaimantCancelWithDefaultResponseReasonBeforeCurrentDate_expect_claimException() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .resolutionThresholdDate(LocalDateTime.now().plusDays(5))
                .completionThresholdDate(LocalDateTime.now().plusDays(5))
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        var requestIdentifier = randomUUID().toString();

        var error = assertThrows(ClaimException.class,
                () -> useCase.execute(claimCancel, true, DEFAULT_RESPONSE, requestIdentifier))
                .getClaimError();

        assertEquals(ClaimError.CLAIMANT_CANCEL_INVALID_REASON, error);

        verify(findByIdPort).find(anyString());
    }

    @Test
    void when_executeWithDonorCancelWithDefaultResponseReasonBeforeCurrentDate_expect_claimException() {
        var claimFindMock = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(12345678)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .resolutionThresholdDate(LocalDateTime.now().plusDays(5))
                .completionThresholdDate(LocalDateTime.now().plusDays(5))
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(findByIdPort.find(anyString())).thenReturn(Optional.of(claimFindMock));

        var claimCancel = Claim.builder()
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .ispb(12345678)
                .build();

        var requestIdentifier = randomUUID().toString();

        var error = assertThrows(ClaimException.class,
                () -> useCase.execute(claimCancel, false, DEFAULT_RESPONSE, requestIdentifier))
                .getClaimError();

        assertEquals(ClaimError.DONOR_CANCEL_INVALID_REASON, error);

        verify(findByIdPort).find(anyString());
    }

}