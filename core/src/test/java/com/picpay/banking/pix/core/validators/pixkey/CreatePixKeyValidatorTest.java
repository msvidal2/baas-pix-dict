package com.picpay.banking.pix.core.validators.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.exception.PixKeyError;
import com.picpay.banking.pix.core.exception.PixKeyException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static com.picpay.banking.pix.core.domain.AccountType.CHECKING;
import static com.picpay.banking.pix.core.domain.CreateReason.CLIENT_REQUEST;
import static com.picpay.banking.pix.core.domain.KeyType.CPF;
import static com.picpay.banking.pix.core.domain.KeyType.EMAIL;
import static com.picpay.banking.pix.core.domain.PersonType.INDIVIDUAL_PERSON;
import static com.picpay.banking.pix.core.domain.PersonType.LEGAL_ENTITY;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePixKeyValidatorTest {

    @InjectMocks
    private CreatePixKeyValidator validator;

    @Mock
    private FindPixKeyPort findPixKeyPort;

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        when(findPixKeyPort.findByAccount(anyString(), anyString(), anyString(), any()))
                .thenReturn(Collections.emptyList());

        when(findPixKeyPort.findPixKey(nullable(String.class), anyString(), nullable(String.class)))
                .thenReturn(null);

        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertDoesNotThrow(() -> {
            validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST);

            verify(findPixKeyPort).findByAccount(anyString(), anyString(), anyString(), any());
            verify(findPixKeyPort).findPixKey(nullable(String.class), anyString(), nullable(String.class));
        });
    }

    @Test
    void when_validateWithNullPixKey_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                validator.validate(randomUUID().toString(), null, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithNullRequestIdentifier_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () ->
                validator.validate(null, pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithEmptyRequestIdentifier_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () ->
                validator.validate("", pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithBlankRequestIdentifier_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () ->
                validator.validate("   ", pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithNullKeyType_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidKey_expect_keyValidatorException() {
        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(KeyValidatorException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidAccountNumber_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456010023456010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidAccountType_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidAccountOpeningDate_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidBranchNumber_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidTaxId_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidIspb_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(0)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidPersonType_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidName_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithNullReason_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(randomUUID().toString(), pixKey, null));
    }

    @Test
    void when_validateWithMaximumNumberKeysReachedIndividualPerson_expect_pixKeyException() {
        var mockKeys = new ArrayList<PixKey>();

        for (int i = 0; i < 5; i++) {
            mockKeys.add(PixKey.builder().build());
        }

        when(findPixKeyPort.findByAccount(anyString(), anyString(), anyString(), any()))
                .thenReturn(mockKeys);

        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(PixKeyException.class, () -> {
            validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST);

            verify(findPixKeyPort).findByAccount(anyString(), anyString(), anyString(), any());
        });
    }

    @Test
    void when_validateWithMaximumNumberKeysReachedLegalPerson_expect_pixKeyException() {
        var mockKeys = new ArrayList<PixKey>();

        for (int i = 0; i < 20; i++) {
            mockKeys.add(PixKey.builder().build());
        }

        when(findPixKeyPort.findByAccount(anyString(), anyString(), anyString(), any()))
                .thenReturn(mockKeys);

        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("empresa@empresa.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(LEGAL_ENTITY)
                .taxId("47025607000190")
                .name("Empresa Teste")
                .build();

        assertThrows(PixKeyException.class, () -> {
            validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST);

            verify(findPixKeyPort).findByAccount(anyString(), anyString(), anyString(), any());
        });
    }

    @Test
    void when_validateIfKeyExists_expect_pixKeyException() {
        var pixKeyMock = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        when(findPixKeyPort.findByAccount(anyString(), anyString(), anyString(), any()))
                .thenReturn(Collections.emptyList());

        when(findPixKeyPort.findPixKey(nullable(String.class), anyString(), nullable(String.class)))
                .thenReturn(pixKeyMock);

        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        var error = assertThrows(PixKeyException.class, () -> {
            validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST);

            verify(findPixKeyPort).findByAccount(anyString(), anyString(), anyString(), any());
            verify(findPixKeyPort).findPixKey(nullable(String.class), anyString(), nullable(String.class));
        }).getPixKeyError();

        assertEquals(error, PixKeyError.KEY_EXISTS);
    }

    @Test
    void when_validateIfKeyExistsIntoPspToSamePerson_expect_pixKeyException() {
        var pixKeyMock = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("2")
                .accountType(CHECKING)
                .accountNumber("934943543")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        when(findPixKeyPort.findByAccount(anyString(), anyString(), anyString(), any()))
                .thenReturn(Collections.emptyList());

        when(findPixKeyPort.findPixKey(nullable(String.class), anyString(), nullable(String.class)))
                .thenReturn(pixKeyMock);

        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        var error = assertThrows(PixKeyException.class, () -> {
            validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST);

            verify(findPixKeyPort).findByAccount(anyString(), anyString(), anyString(), any());
            verify(findPixKeyPort).findPixKey(nullable(String.class), anyString(), nullable(String.class));
        }).getPixKeyError();

        assertEquals(error, PixKeyError.KEY_EXISTS_INTO_PSP_TO_SAME_PERSON);
    }

    @Test
    void when_validateIfKeyExistsIntoPspToAnotherPerson_expect_pixKeyException() {
        var pixKeyMock = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("2")
                .accountType(CHECKING)
                .accountNumber("934943543")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("22222222222")
                .name("Joao da Silva")
                .build();

        when(findPixKeyPort.findByAccount(anyString(), anyString(), anyString(), any()))
                .thenReturn(Collections.emptyList());

        when(findPixKeyPort.findPixKey(nullable(String.class), anyString(), nullable(String.class)))
                .thenReturn(pixKeyMock);

        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        var error = assertThrows(PixKeyException.class, () -> {
            validator.validate(randomUUID().toString(), pixKey, CLIENT_REQUEST);

            verify(findPixKeyPort).findByAccount(anyString(), anyString(), anyString(), any());
            verify(findPixKeyPort).findPixKey(nullable(String.class), anyString(), nullable(String.class));
        }).getPixKeyError();

        assertEquals(error, PixKeyError.KEY_EXISTS_INTO_PSP_TO_ANOTHER_PERSON);
    }

//    @Test
//    void when_validateIfClaimExists_expect_pixKeyException() {
//        fail();
//    }

}