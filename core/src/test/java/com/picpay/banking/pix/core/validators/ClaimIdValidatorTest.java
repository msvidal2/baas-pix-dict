package com.picpay.banking.pix.core.validators;

import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.ClaimIdValidator.validate;
import static org.junit.jupiter.api.Assertions.*;

class ClaimIdValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate("7b8d0484-b13c-496e-ba05-f7889c358132"));
    }

    @Test
    void when_validateWithNull_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(null));
    }

    @Test
    void when_validateWithEmpty_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(""));
    }

    @Test
    void when_validateWithBlank_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate("  "));
    }

    @Test
    void when_validateWithInvalid_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate("7b8d0484-b13c-496e-ba05-f7889"));
    }

}