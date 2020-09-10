package com.picpay.banking.pix.core.validators.key;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CNPJKeyValidatorTest {

    private CNPJKeyValidator validator;

    @BeforeEach
    public void setup() {
        validator = new CNPJKeyValidator();
    }

    @Test
    public void testValidator() {
        Assertions.assertDoesNotThrow(() -> validator.validate("68011649000141"));
    }

    @Test
    public void testValidatorNull() {
        assertThrows(KeyValidatorException.class, () -> validator.validate(null));
    }

    @Test
    public void testValidatorEmpty() {
        assertThrows(KeyValidatorException.class, () -> validator.validate(""));
    }

    @Test
    public void testValidatorWithPoints() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("90.536.249/0001-09"));
    }

    @Test
    public void testValidatorInvalid() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("90536249000100"));
    }

}
