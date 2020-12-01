package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimFantasyNameItemValidator {

    public static void validate(String fantasyName) {
        if (fantasyName == null) {
            return;
        }

        if (fantasyName.length() > 100) {
            throw new IllegalArgumentException("The number of characters in the fantasyName must be less than 100");
        }
    }
}
