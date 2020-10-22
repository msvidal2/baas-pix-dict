package com.picpay.banking.pix.core.validators.claim;

import com.picpay.banking.pix.core.domain.Claim;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClaimIdItemValidatorTest {

    @Test
    void when_validateWithSuccess_expect_noExceptions() {
        var claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f7889c358132")
                .build();

        assertDoesNotThrow(() -> new ClaimIdItemValidator().validate(claim));
    }

    @Test
    void when_validateWithUppercaseClaimId_expect_noExceptions() {
        var claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05-f7889c358132")
                .build();

        assertDoesNotThrow(() -> new ClaimIdItemValidator().validate(claim));
    }

    @Test
    void when_validateWithInvalidClaimId_expect_exceptions() {
        var claim = Claim.builder()
                .claimId("7b8d0484-b13c-496e-ba05")
                .build();

        assertThrows(IllegalArgumentException.class, () -> new ClaimIdItemValidator().validate(claim));
    }

    @Test
    void when_validateWithEmptyClaimId_expect_exceptions() {
        var claim = Claim.builder()
                .claimId("")
                .build();

        assertThrows(IllegalArgumentException.class, () -> new ClaimIdItemValidator().validate(claim));
    }

    @Test
    void when_validateWithNullClaimId_expect_exceptions() {
        var claim = Claim.builder()
                .claimId(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> new ClaimIdItemValidator().validate(claim));
    }

}