package com.picpay.banking.pix.core.validators.key;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CellPhoneKeyValidatorTest {

    private CellPhoneKeyValidator validator;

    @BeforeEach
    public void setup() {
         validator = new CellPhoneKeyValidator();
    }

    @Test
    public void testValidate() {
        Assertions.assertDoesNotThrow(() -> validator.validate("+5511945371633"));
    }

    @Test
    public void testValidateNull() {
        assertThrows(KeyValidatorException.class, () -> validator.validate(null));
    }

    @Test
    public void testValidateEmpty() {
        assertThrows(KeyValidatorException.class, () -> validator.validate(""));
    }

    @Test
    public void testValidateWithoutCountryCode() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("11945371633"));
    }

    @Test
    public void testValidateWithoutPlus() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("5511945371633"));
    }

    @Test
    public void testValidateWithoutCountryCodeAndDDD() {
        assertThrows(KeyValidatorException.class, () -> validator.validate("945371633"));
    }

}
