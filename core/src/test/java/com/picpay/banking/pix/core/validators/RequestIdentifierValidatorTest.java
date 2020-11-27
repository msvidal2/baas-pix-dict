package com.picpay.banking.pix.core.validators;

import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.RequestIdentifierValidator.validate;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestIdentifierValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate(randomUUID().toString()));
    }

    @Test
    void when_validateWithNullRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(null));
    }

    @Test
    void when_validateWithEmptyRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(""));
    }

    @Test
    void when_validateWithBlankRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate("   "));
    }

}