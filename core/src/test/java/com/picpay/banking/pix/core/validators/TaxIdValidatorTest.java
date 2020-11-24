package com.picpay.banking.pix.core.validators;

import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.TaxIdValidator.validate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaxIdValidatorTest {

    @Test
    void when_validateWithCPFSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate("11111111111"));
    }

    @Test
    void when_validateWithCNPJSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate("11111111111111"));
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
        assertThrows(IllegalArgumentException.class, () -> validate(" "));
    }

    @Test
    void when_validateWithValueGreaterThan14_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate("111111111111112"));
    }

}