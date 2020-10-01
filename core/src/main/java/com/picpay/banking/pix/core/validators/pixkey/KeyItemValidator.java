package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

import java.util.Optional;

public class KeyItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey domain) {
        if(KeyType.RANDOM.equals(domain.getType())) {
            if (domain.getKey() == null || domain.getKey().isEmpty()) {
                return;
            }
            new IllegalArgumentException("The key must be null");
        }

        Optional.ofNullable(domain.getType())
                .orElseThrow(() -> new IllegalArgumentException("The key type cannot be null"))
                .getValidator()
                .validate(domain.getKey());
    }
}
