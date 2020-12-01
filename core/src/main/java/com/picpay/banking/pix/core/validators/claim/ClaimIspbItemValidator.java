package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimIspbItemValidator {

    public static void validate(int ispb) {
        if (ispb <= 0) {
            throw new IllegalArgumentException("Invalid ISPB");
        }
    }
}
