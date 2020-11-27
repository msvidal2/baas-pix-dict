package com.picpay.banking.pix.core.validators.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class ClaimCpfCnpjItemValidator implements DictItemValidator<Claim> {

    @Override
    public void validate(Claim domain) {
        if (Strings.isNullOrEmpty(String.valueOf(domain.getCpfCnpj()))) {
            throw new IllegalArgumentException("cpfCnpj can not be empty");
        }

        if (String.valueOf(domain.getCpfCnpj()).length() > 14) {
            throw new IllegalArgumentException("The number of characters in the cpfCnpj must be less than 15");
        }
    }
}
