package com.picpay.banking.pix.core.validators.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.core.domain.AccountType.CHECKING;
import static com.picpay.banking.pix.core.domain.Reason.CLIENT_REQUEST;
import static com.picpay.banking.pix.core.domain.KeyType.*;
import static com.picpay.banking.pix.core.domain.PersonType.INDIVIDUAL_PERSON;
import static com.picpay.banking.pix.core.validators.pixkey.CreatePixKeyValidator.validate;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreatePixKeyValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
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

        assertDoesNotThrow(() -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithSuccessRandom_expect_noExceptions() {
        var pixKey = PixKey.builder()
                .type(RANDOM)
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertDoesNotThrow(() -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithNullPixKey_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), null, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(null, pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate("", pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate("   ", pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(KeyValidatorException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
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

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixKey, null));
    }

}