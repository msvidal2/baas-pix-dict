package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.AccountTypeOriginal;
import com.picpay.banking.pix.original.dto.KeyTypeOriginal;
import com.picpay.banking.pix.original.dto.response.ClaimDTO;
import com.picpay.banking.pix.original.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.original.dto.response.ClaimerAccountDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateClaimPortImplTest {

    @InjectMocks
    private CreateClaimPortImpl port;

    @Mock
    private ClaimClient claimClient;

    private Claim claim;

    private ClaimResponseDTO claimResponseDTO;

    @BeforeEach
    void setup() {
        claim = Claim.builder()
                .accountNumber("123456")
                .accountType(AccountType.CHECKING)
                .branchNumber("0001")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .key("+5511998765499")
                .keyType(KeyType.CELLPHONE)
                .name("Deutonio Celso da Silva")
                .ispb(92894922)
                .cpfCnpj("12345678902")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .build();

        claimResponseDTO = ClaimResponseDTO.builder()
                .claim(ClaimDTO.builder()
                        .claimerAccount(ClaimerAccountDTO.builder()
                                .accountNumber(123456)
                                .accountType(AccountTypeOriginal.CACC)
                                .branch("0001")
                                .participant(92894922)
                                .build())
                        .completionPeriodEnd("string")
                        .donorParticipant("string")
                        .id("75")
                        .key("+5511998765499")
                        .keyType(KeyTypeOriginal.PHONE)
                        .lastModified("string")
                        .resolutionPeriodEnd("string")
                        .status("OPEN")
                        .type("OWNERSHIP")
                        .build())
                .signature("string")
                .build();
    }

    @Test
    void when_createClaimWithSuccess_expect_claim() {
        var responseWrapper = ResponseWrapperDTO.<ClaimResponseDTO>builder()
                .data(claimResponseDTO)
                .build();

        when(claimClient.create(any())).thenReturn(responseWrapper);

        assertDoesNotThrow(() -> {
            var response = port.createPixKey(claim, randomUUID().toString());

            assertNotNull(response);
            assertEquals("+5511998765499", response.getKey());
            assertEquals(KeyType.CELLPHONE, response.getKeyType());
            assertEquals("75", response.getClaimId());
            assertEquals("123456", response.getAccountNumber());
            assertEquals(AccountType.CHECKING, response.getAccountType());
            assertEquals("0001", response.getBranchNumber());
            assertEquals(92894922, response.getIspb());
        });
    }

    @Test
    void when_createClaimWithoutResponseData_expect_null() {
        var responseWrapper = ResponseWrapperDTO.<ClaimResponseDTO>builder().build();

        when(claimClient.create(any())).thenReturn(responseWrapper);

        assertDoesNotThrow(() -> {
            var response = port.createPixKey(claim, randomUUID().toString());

            assertNull(response);
        });
    }

}