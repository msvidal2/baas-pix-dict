package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPixKeyUseCaseTest {

    private static final String randomUUID = randomUUID().toString();

    @InjectMocks
    private FindPixKeyUseCase useCase;

    @Mock
    private FindPixKeyPort findPixKeyPort;

    @Mock
    private FindPixKeyBacenPort findPixKeyBacenPort;

    @Test
    void when_findPixKeyWithSuccess_expect_pixKey() {
        var pixKeyMockResponse = PixKey.builder()
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .accountType(AccountType.CHECKING)
                .branchNumber("0001")
                .claim(ClaimType.POSSESSION_CLAIM)
                .taxId("59375566072")
                .createdAt(LocalDateTime.now())
                .ispb(5345343)
                .key("59375566072")
                .name("Dona Maria")
                .nameIspb("PicPay Bank")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .startPossessionAt(LocalDateTime.now())
                .type(KeyType.CPF)
                .build();

        when(findPixKeyPort.findPixKey(any()))
                .thenReturn(pixKeyMockResponse);

        assertDoesNotThrow(() -> {
            var pixKey = useCase.execute(randomUUID, "59375566072", "59375566072");

            assertNotNull(pixKey);
            assertEquals(PersonType.INDIVIDUAL_PERSON, pixKey.getPersonType());
            assertEquals(KeyType.CPF, pixKey.getType());
            assertEquals("59375566072", pixKey.getKey());
        });
    }

    @Test
    void when_findPixKeyBacenWithSuccess_expect_pixKey() {
        var pixKeyMockResponse = PixKey.builder()
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .accountType(AccountType.CHECKING)
                .branchNumber("0001")
                .claim(ClaimType.POSSESSION_CLAIM)
                .taxId("59375566072")
                .createdAt(LocalDateTime.now())
                .ispb(5345343)
                .key("59375566072")
                .name("Dona Maria")
                .nameIspb("PicPay Bank")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .startPossessionAt(LocalDateTime.now())
                .type(KeyType.CPF)
                .build();

        when(findPixKeyPort.findPixKey(any()))
                .thenReturn(null);

        when(findPixKeyBacenPort.findPixKey(any(), any(), any()))
                .thenReturn(pixKeyMockResponse);

        assertDoesNotThrow(() -> {
            var pixKey = useCase.execute(randomUUID, "59375566072", "59375566072");

            assertNotNull(pixKey);
            assertEquals(PersonType.INDIVIDUAL_PERSON, pixKey.getPersonType());
            assertEquals(KeyType.CPF, pixKey.getType());
            assertEquals("59375566072", pixKey.getKey());
        });
    }

    @Test
    void when_findPixKeyWithoutPixKey_expect_exception() {
        assertThrows(NullPointerException.class, () ->
            useCase.execute(randomUUID, null, "59375566072"));
    }

    @Test
    void when_findPixKeyWithoutUseId_expect_exception() {
        assertThrows(NullPointerException.class, () ->
                useCase.execute(randomUUID, "59375566072", null));
    }

    @Test
    void when_findPixKeyWithEmptyUseId_expect_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(randomUUID, "59375566072", ""));
    }

}