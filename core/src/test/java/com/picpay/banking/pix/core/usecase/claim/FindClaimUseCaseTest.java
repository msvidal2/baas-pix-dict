package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindClaimUseCaseTest {

    @InjectMocks
    private FindClaimUseCase findClaimUseCase;

    @Mock
    private FindClaimPort findClaimPort;

    @Test
    void when_findClaimWithSuccess_expect_equalResults() {
        var claim = Claim.builder()
                .accountNumber("123456")
                .accountType(AccountType.CHECKING)
                .branchNumber("0001")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .key("+5511998765499")
                .keyType(KeyType.PHONE)
                .name("Deutonio Celso da Silva")
                .ispb(92894922)
                .taxId("12345678902")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .build();

        when(findClaimPort.findClaim(anyString(), anyString(), anyBoolean())).thenReturn(claim);

        assertDoesNotThrow(() -> {
            var response = findClaimUseCase.execute("123456", "123", false);

            assertNotNull(response);
            assertEquals(response.getAccountNumber(), claim.getAccountNumber());
            assertEquals(response.getAccountType(), claim.getAccountType());
            assertEquals(response.getClaimType(), claim.getClaimType());
            assertEquals(response.getKey(), claim.getKey());
            assertEquals(response.getKeyType(), claim.getKeyType());
            assertEquals(response.getName(), claim.getName());
            assertEquals(response.getIspb(), claim.getIspb());
            assertEquals(response.getTaxId(), claim.getTaxId());
            assertEquals(response.getPersonType(), claim.getPersonType());
        });
    }

}
