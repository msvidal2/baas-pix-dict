package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.AccountTypeOriginal;
import com.picpay.banking.pix.original.dto.KeyTypeOriginal;
import com.picpay.banking.pix.original.dto.response.ClaimDTO;
import com.picpay.banking.pix.original.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.original.dto.response.ClaimerAccountDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.exception.NotFoundOriginalClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindClaimPortImplTest {

    @InjectMocks
    private FindClaimPortImpl port;

    @Mock
    private ClaimClient claimClient;

    private ResponseWrapperDTO<List<ClaimResponseDTO>> claimResponseDTO;

    @BeforeEach
    void setup() {
        claimResponseDTO = ResponseWrapperDTO.<List<ClaimResponseDTO>>builder()
                .data(List.of(ClaimResponseDTO.builder()
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
                        .build()))
                .build();
    }

    @Test
    void when_findClaimWithExistentId_expect_equalResults() {
        when(claimClient.find()).thenReturn(claimResponseDTO);

        assertDoesNotThrow(() -> {
            var response = port.findClaim("75", "123", false);

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
    void when_findClaimWithNonExistentId_expect_notFoundException() {
        when(claimClient.find()).thenReturn(claimResponseDTO);

        assertThrows(NotFoundOriginalClientException.class,() -> port.findClaim("999999", "123", false));
    }

}
