package com.picpay.banking.pix.core.validators.addressingkey;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

import java.util.Optional;

public class KeyItemValidator implements DictItemValidator<AddressingKey> {

    @Override
    public void validate(AddressingKey domain) {
        Optional.ofNullable(domain.getType())
                .orElseThrow(() -> new IllegalArgumentException("The key type cannot be null"))
                .getValidator()
                .validate(domain.getKey());
    }
}
