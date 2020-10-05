package com.picpay.banking.pix.core.validators.key;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailKeyValidatorTest {

    private EmailKeyValidator validator;

    @BeforeEach
    public void setup() {
        validator = new EmailKeyValidator();
    }

    @Test
    public void testValidator() {
        Assertions.assertDoesNotThrow(() -> validator.validate("teste@teste.com"));
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
    public void testValidatorBiggerThen72() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("snigcmwnituyvb384bty83bv7y4tn4387v485ht84n5f83n235erglnsviut843ht8@test.com"));
    }

    @Test
    public void testValidatorInvalid() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("test@"));
    }

    @Test
    public void testValidatorInvalid2() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("testteste.com"));
    }

}
