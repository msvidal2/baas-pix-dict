package com.picpay.banking.pix.core.validators;

import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.NameValidator.validate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NameValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate("Maria do Socorro"));
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
    void when_validateWithValueGreaterThan100_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                validate(Strings.padStart("", 101, 'A')));
    }

}