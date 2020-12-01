package com.picpay.banking.pix.core.validators.claim;

import com.picpay.banking.pix.core.domain.Claim;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.picpay.banking.pix.core.domain.ClaimCancelReason.CLIENT_REQUEST;
import static com.picpay.banking.pix.core.validators.claim.ClaimCancelValidator.validate;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClaimCancelValidatorTest {

    private Claim claim;

    @BeforeEach
    void setup() {
        claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f7889c358132")
                .ispb(24323434)
                .build();
    }

    @Test
    void when_validateWithClaimantSuccess_expect_noExceptions() {
        assertDoesNotThrow(() -> validate(claim, true, CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    void when_validateWithNullRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(claim, false, CLIENT_REQUEST, null));
    }

    @Test
    void when_validateWithEmptyRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(claim, false, CLIENT_REQUEST, ""));
    }

    @Test
    void when_validateWithBlankRequestIdentifier_expect_illegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validate(claim, false, CLIENT_REQUEST, "  "));
    }

    @Test
    void when_validateWithInvalidClaimId_expect_illegalArgumentException() {
        var claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f78")
                .ispb(24323434)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(claim, false, CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    void when_validateWithInvalidClaimantIspb_expect_illegalArgumentException() {
        var claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f7889c358132")
                .ispb(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(claim, true, CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    void when_validateWithInvalidDonorIspb_expect_illegalArgumentException() {
        var claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f7889c358132")
                .ispb(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(claim, false, CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    void when_validateWithNullReason_expect_illegalArgumentException() {
        var claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f7889c358132")
                .ispb(24323434)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validate(claim, true, null, randomUUID().toString()));
    }

}