package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListPixKeyUseCaseTest {

    @InjectMocks
    private ListPixKeyUseCase useCase;

    @Mock
    private ListPixKeyPort listPixKeyPort;

    @Test
    void when_listPixKeyWithSuccess_expect_listOfPixKeys() {
        var pixKeyMock = PixKey.builder()
            .accountNumber("241242323")
            .accountOpeningDate(LocalDateTime.now())
            .accountType(AccountType.CHECKING)
            .branchNumber("0001")
            .claim(ClaimType.POSSESSION_CLAIM)
            .taxId("62088010017")
            .createdAt(LocalDateTime.now())
            .ispb(25325344)
            .key("62088010017")
            .name("Dona Maria")
            .nameIspb("PicPay Bank")
            .personType(PersonType.INDIVIDUAL_PERSON)
            .startPossessionAt(LocalDateTime.now())
            .type(KeyType.CPF)
            .build();

        when(listPixKeyPort.listPixKey(anyString(), any())).thenReturn(List.of(pixKeyMock));

        var pixKey = PixKey.builder()
                .taxId("62088010017")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("241242323")
                .ispb(25325344)
                .build();

        assertDoesNotThrow(() -> {
            var listPixKeyResponse = useCase.execute(randomUUID().toString(), pixKey);

            assertNotNull(listPixKeyResponse);
            assertEquals(1, listPixKeyResponse.size());

            var pixKeyReturned = listPixKeyResponse.get(0);

            assertEquals(pixKey.getTaxId(), pixKeyReturned.getTaxId());
            assertEquals(pixKey.getPersonType(), pixKeyReturned.getPersonType());
            assertEquals(pixKey.getBranchNumber(), pixKeyReturned.getBranchNumber());
            assertEquals(pixKey.getAccountType(), pixKeyReturned.getAccountType());
            assertEquals(pixKey.getAccountNumber(), pixKeyReturned.getAccountNumber());
            assertEquals(pixKey.getIspb(), pixKeyReturned.getIspb());
        });

        verify(listPixKeyPort).listPixKey(anyString(), any());
    }

    @Test
    void when_validateWithValidationError_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .taxId("62088010017")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber(null)
                .ispb(25325344)
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(randomUUID().toString(), pixKey));
    }

}