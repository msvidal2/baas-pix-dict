package com.picpay.banking.pix.core.validators;

import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.IspbValidator.validate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IspbValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate(1231234));
    }

    @Test
    void when_validateWithNullValue_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(null));
    }

    @Test
    void when_validateWithValueGreaterThanEight_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(123456789));
    }

}