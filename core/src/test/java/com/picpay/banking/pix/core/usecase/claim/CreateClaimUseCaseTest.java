package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CreateClaimUseCaseTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    @InjectMocks
    private CreateClaimUseCase useCase;

    @Mock
    private CreateClaimBacenPort createClaimPort;

    @Mock
    private CreateClaimPort saveClaimPort;

    @Mock
    private FindOpenClaimByKeyPort findOpenClaimByKeyPort;

    @Mock
    private FindPixKeyPort findPixKeyPort;

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
                .donorIspb(87654321)
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .completionThresholdDate(NOW.plusDays(7))
                .resolutionThresholdDate(NOW.plusDays(7))
                .lastModifiedDate(NOW)
                .build();
    }

    @Test
    void when_createClaimWithSuccess_expect_claimId() {
        when(findOpenClaimByKeyPort.find(anyString())).thenReturn(Optional.empty());
        when(findPixKeyPort.findPixKey(anyString())).thenReturn(Optional.empty());
        when(createClaimPort.createClaim(any(), anyString())).thenReturn(claimResponse);
        when(saveClaimPort.saveClaim(any(), anyString())).thenReturn(claimResponse);

        Claim claimCreated = useCase.execute(claimRequest, randomUUID().toString());

        assertEquals(claimResponse.getClaimId(), claimCreated.getClaimId());

        verify(findOpenClaimByKeyPort, times(1)).find(anyString());
        verify(findPixKeyPort, times(1)).findPixKey(anyString());
        verify(createClaimPort, times(1)).createClaim(any(), anyString());
        verify(saveClaimPort, times(1)).saveClaim(any(), anyString());
    }

    @Test
    void when_createClaimForKeyWithAlreadyOpenClaim_expect_claimException() {
        when(findOpenClaimByKeyPort.find(anyString())).thenReturn(Optional.of(claimResponse));

        assertThrows(ClaimException.class, () -> useCase.execute(claimRequest, randomUUID().toString()));

        verify(findOpenClaimByKeyPort, times(1)).find(anyString());
        verify(findPixKeyPort, times(0)).findPixKey(anyString());
        verify(createClaimPort, times(0)).createClaim(any(), anyString());
        verify(saveClaimPort, times(0)).saveClaim(any(), anyString());
    }

    @Test
    void when_createClaimForKeyAlreadyOwnedByCostumer_expect_claimException() {
        when(findOpenClaimByKeyPort.find(anyString())).thenReturn(Optional.empty());

        when(findPixKeyPort.findPixKey(anyString())).thenReturn(
                Optional.of(
                        PixKey.builder()
                                .taxId(claimRequest.getCpfCnpj())
                                .build()));

        assertThrows(ClaimException.class, () -> useCase.execute(claimRequest, randomUUID().toString()));

        verify(findOpenClaimByKeyPort, times(1)).find(anyString());
        verify(findPixKeyPort, times(1)).findPixKey(anyString());
        verify(createClaimPort, times(0)).createClaim(any(), anyString());
        verify(saveClaimPort, times(0)).saveClaim(any(), anyString());
    }

    @Test
    void when_createPortabilityClaimForKeyWithDifferentTaxId_expect_claimException() {
        claimRequest.setClaimType(ClaimType.PORTABILITY);

        PixKey keyWithDifferentTaxId = PixKey.builder()
                .taxId("13050132043")
                .build();

        when(findOpenClaimByKeyPort.find(anyString())).thenReturn(Optional.empty());

        when(findPixKeyPort.findPixKey(anyString())).thenReturn(Optional.of(keyWithDifferentTaxId));

        assertThrows(ClaimException.class, () -> useCase.execute(claimRequest, randomUUID().toString()));

        verify(findOpenClaimByKeyPort, times(1)).find(anyString());
        verify(findPixKeyPort, times(1)).findPixKey(anyString());
        verify(createClaimPort, times(0)).createClaim(any(), anyString());
        verify(saveClaimPort, times(0)).saveClaim(any(), anyString());
    }

}
