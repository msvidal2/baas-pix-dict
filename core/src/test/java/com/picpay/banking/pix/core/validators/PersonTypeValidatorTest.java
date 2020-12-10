package com.picpay.banking.pix.core.validators;

import com.picpay.banking.pix.core.domain.PersonType;
import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.validators.PersonTypeValidator.validate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTypeValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noException() {
        assertDoesNotThrow(() -> validate(PersonType.INDIVIDUAL_PERSON));
    }

    @Test
    void when_validateWithNullValue_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(null));
    }

}