package com.picpay.banking.pix.core.validators;

import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.BranchNumberValidator.validate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BranchNumberValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate("1"));
    }

    @Test
    void when_validateWithNullValue_expect_noExceptions() {
        assertDoesNotThrow(() -> validate(null));
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
    void when_validateWithValueLessThanOne_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(""));
    }

    @Test
    void when_validateWithValueGreaterThanFour_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate("12345"));
    }

}