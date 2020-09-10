package com.picpay.banking.pix.core.validators.pixkey;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class CpfCnpjItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey domain) {
        if (Strings.isNullOrEmpty(String.valueOf(domain.getCpfCnpj()))) {
            throw new IllegalArgumentException("cpfCnpj can not be empty");
        }

        if (String.valueOf(domain.getCpfCnpj()).length() > 14) {
            throw new IllegalArgumentException("The number of characters in the cpfCnpj must be less than 15");
        }
    }
}
