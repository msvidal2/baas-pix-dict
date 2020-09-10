package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.FindPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPixKeyUseCaseTest {

    @InjectMocks
    private FindPixKeyUseCase useCase;

    @Mock
    private FindPixKeyPort findPixKeyPort;

    @Mock
    private DictItemValidator validator;

    @Test
    void when_findPixKeyWithSuccess_expect_pixKey() {
        var pixKeyMockResponse = PixKey.builder()
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .accountType(AccountType.CHECKING)
                .branchNumber("0001")
                .claim(ClaimType.POSSESION_CLAIM)
                .cpfCnpj(59375566072L)
                .createdAt(LocalDateTime.now())
                .ispb(5345343)
                .key("59375566072")
                .name("Dona Maria")
                .nameIspb("PicPay Bank")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .startPossessionAt(LocalDateTime.now())
                .type(KeyType.CPF)
                .build();

        when(findPixKeyPort.findPixKey(any(), anyString()))
                .thenReturn(pixKeyMockResponse);

        var pixKeyRequest = PixKey.builder()
                .key("59375566072")
                .build();

        assertDoesNotThrow(() -> {
            var pixKey = useCase.findPixKeyUseCase(pixKeyRequest, "59375566072");

            assertNotNull(pixKey);
            assertEquals(PersonType.INDIVIDUAL_PERSON, pixKey.getPersonType());
            assertEquals(KeyType.CPF, pixKey.getType());
            assertEquals("59375566072", pixKey.getKey());
        });
    }

    @Test
    void when_findPixKeyWithoutPixKey_expect_exception() {
        assertThrows(NullPointerException.class, () ->
            useCase.findPixKeyUseCase(null, "59375566072"));
    }

    @Test
    void when_findPixKeyWithoutUseId_expect_exception() {
        var pixKeyRequest = PixKey.builder()
                .key("59375566072")
                .build();

        assertThrows(NullPointerException.class, () ->
                useCase.findPixKeyUseCase(pixKeyRequest, null));
    }

    @Test
    void when_findPixKeyWithEmptyUseId_expect_exception() {
        var pixKeyRequest = PixKey.builder()
                .key("59375566072")
                .build();

        assertThrows(IllegalArgumentException.class, () ->
                useCase.findPixKeyUseCase(pixKeyRequest, ""));
    }

}