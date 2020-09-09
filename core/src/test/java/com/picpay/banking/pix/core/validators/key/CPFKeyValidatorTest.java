package com.picpay.banking.pix.core.validators.key;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CPFKeyValidatorTest {

    private CPFKeyValidator validator;

    @BeforeEach
    public void setup() {
        validator = new CPFKeyValidator();
    }

    @Test
    public void testValidator() {
        Assertions.assertDoesNotThrow(() -> validator.validate("02942765054"));
    }

    @Test
    public void testValidatorInvalid() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("461.398.510-00"));
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
        assertThrows(KeyValidatorException.class, () -> validator.validate("461.398.510-25"));
    }

    @Test
    public void testValidatorWithLettres() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("4613ds9851025"));
    }
}
