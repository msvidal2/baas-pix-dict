package com.picpay.banking.pix.core.validators;

import com.picpay.banking.pix.core.domain.AccountType;
import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.AccountTypeValidator.validate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTypeValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate(AccountType.CHECKING));
    }

    @Test
    void when_validateWithNullValue_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(null));
    }

}