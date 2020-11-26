package com.picpay.banking.pix.core.validators;

import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.AccountNumberValidator.validate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountNumberValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate("123456"));
    }

    @Test
    void when_validateWithNullValue_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(null));
    }

    @Test
    void when_validateWithEmptyValue_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(""));
    }

    @Test
    void when_validateWithBlankValue_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate("   "));
    }

    @Test
    void when_validateWithValueLengthLessThanOne_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(""));
    }

    @Test
    void when_validateWithValueLengthGreaterThan20_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate("123456789012345678901"));
    }
    
}