package com.picpay.banking.pix.core.validators;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.core.validators.AccountOpeningDateValidator.validate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountOpeningDateValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate(LocalDateTime.now()));
    }

    @Test
    void when_validateWithNullValue_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(null));
    }
    
}