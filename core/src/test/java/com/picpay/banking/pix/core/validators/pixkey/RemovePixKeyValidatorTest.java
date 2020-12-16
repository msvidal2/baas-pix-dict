package com.picpay.banking.pix.core.validators.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.picpay.banking.pix.core.domain.RemoveReason.CLIENT_REQUEST;
import static com.picpay.banking.pix.core.validators.pixkey.RemovePixKeyValidator.validate;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

class RemovePixKeyValidatorTest {

    private PixKey pixKey;

    @BeforeEach
    void setup() {
        pixKey = PixKey.builder()
                .key("email@email.com")
                .type(KeyType.EMAIL)
                .ispb(12345678)
                .build();
    }

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithNullRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> validate(null, pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithEmptyRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> validate("", pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithBlankRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> validate("    ", pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithNullPixKey_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> validate(randomUUID().toString(), null, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithNullKeyType_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .key("email@email.com")
                .type(null)
                .ispb(12345678)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidKey_expect_keyValidatorException() {
        var pixKey = PixKey.builder()
                .key("")
                .type(KeyType.EMAIL)
                .ispb(12345678)
                .build();

        assertThrows(KeyValidatorException.class,
                () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithInvalidIspb_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .key("email@email.com")
                .type(KeyType.EMAIL)
                .ispb(0)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validate(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_validateWithNullReason_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .key("email@email.com")
                .type(KeyType.EMAIL)
                .ispb(12345678)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validate(randomUUID().toString(), pixKey, null));
    }

}