package com.picpay.banking.pix.core.validators.claim;

import com.picpay.banking.pix.core.domain.Claim;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.domain.ClaimConfirmationReason.CLIENT_REQUEST;
import static com.picpay.banking.pix.core.validators.claim.ConfirmClaimValidator.validate;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfirmClaimValidatorTest {

    private Claim claim;

    private String requestIdentifier;

    @BeforeEach
    void setup() {
        claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f7889c358132")
                .ispb(24323434)
                .build();

        requestIdentifier = randomUUID().toString();
    }

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate(claim, CLIENT_REQUEST, requestIdentifier));
    }

    @Test
    void when_validateWithNullRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(claim, CLIENT_REQUEST, null));
    }

    @Test
    void when_validateWithEmptyRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(claim, CLIENT_REQUEST, ""));
    }

    @Test
    void when_validateWithBlankRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(claim, CLIENT_REQUEST, "   "));
    }

    @Test
    void when_validateWithInvalidClaimId_expect_illegalArgumentException() {
        var claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f7889c3")
                .ispb(24323434)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(claim, CLIENT_REQUEST, requestIdentifier));
    }

    @Test
    void when_validateWithInvalidIspb_expect_illegalArgumentException() {
        var claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f7889c358132")
                .ispb(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(claim, CLIENT_REQUEST, requestIdentifier));
    }

    @Test
    void when_validateWithNullReason_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(claim, null, requestIdentifier));
    }

}