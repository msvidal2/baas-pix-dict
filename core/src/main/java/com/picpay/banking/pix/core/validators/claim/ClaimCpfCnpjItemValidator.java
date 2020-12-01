package com.picpay.banking.pix.core.validators.claim;

import com.google.common.base.Strings;

public class ClaimCpfCnpjItemValidator {

    public static void validate(String cpfCnpj) {
        if (Strings.isNullOrEmpty(cpfCnpj)) {
            throw new IllegalArgumentException("cpfCnpj can not be empty");
        }

        if (cpfCnpj.length() > 14) {
            throw new IllegalArgumentException("The number of characters in the cpfCnpj must be less than 15");
        }
    }
}
