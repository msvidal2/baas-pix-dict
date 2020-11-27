package com.picpay.banking.pix.core.validators.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimCpfCnpjItemValidator {

    public static void validate(String domain) {
        if (Strings.isNullOrEmpty(domain)) {
            throw new IllegalArgumentException("cpfCnpj can not be empty");
        }

        if (domain.length() > 14) {
            throw new IllegalArgumentException("The number of characters in the cpfCnpj must be less than 15");
        }
    }
}
