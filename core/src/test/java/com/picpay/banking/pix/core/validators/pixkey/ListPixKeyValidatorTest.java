package com.picpay.banking.pix.core.validators.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.pixkey.ListPixKeyValidator.validate;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

class ListPixKeyValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        var pixkey = PixKey.builder()
                .taxId("62088010017")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("241242323")
                .ispb(25325344)
                .build();

        assertDoesNotThrow(() -> validate(randomUUID().toString(), pixkey));
    }

    @Test
    void when_validateWithoutBranch_expect_noExceptions() {
        var pixkey = PixKey.builder()
                .taxId("62088010017")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber(null)
                .accountType(AccountType.CHECKING)
                .accountNumber("241242323")
                .ispb(25325344)
                .build();

        assertDoesNotThrow(() -> validate(randomUUID().toString(), pixkey));
    }

    @Test
    void when_validateWithInvalidRequestIdentifier_expect_illegalArgumentException() {
        var pixkey = PixKey.builder()
                .taxId("62088010017")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("241242323")
                .ispb(25325344)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(null, pixkey));
    }

    @Test
    void when_validateWithInvalidTaxId_expect_illegalArgumentException() {
        var pixkey = PixKey.builder()
                .taxId("")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("241242323")
                .ispb(25325344)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixkey));
    }

    @Test
    void when_validateWithInvalidPersonType_expect_illegalArgumentException() {
        var pixkey = PixKey.builder()
                .taxId("62088010017")
                .personType(null)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("241242323")
                .ispb(25325344)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixkey));
    }

    @Test
    void when_validateWithInvalidBranchNumber_expect_illegalArgumentException() {
        var pixkey = PixKey.builder()
                .taxId("62088010017")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber("")
                .accountType(AccountType.CHECKING)
                .accountNumber("241242323")
                .ispb(25325344)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixkey));
    }

    @Test
    void when_validateWithInvalidAccountNumber_expect_illegalArgumentException() {
        var pixkey = PixKey.builder()
                .taxId("62088010017")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("22353453453453453534534541242323")
                .ispb(25325344)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixkey));
    }

    @Test
    void when_validateWithInvalidAccountType_expect_illegalArgumentException() {
        var pixkey = PixKey.builder()
                .taxId("62088010017")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber("0001")
                .accountType(null)
                .accountNumber("241242323")
                .ispb(25325344)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixkey));
    }

    @Test
    void when_validateWithInvalidIspb_expect_illegalArgumentException() {
        var pixkey = PixKey.builder()
                .taxId("62088010017")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("241242323")
                .ispb(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(randomUUID().toString(), pixkey));
    }

}