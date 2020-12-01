package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.key.KeyValidator;

import java.util.Optional;

public class KeyItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey pixKey) {
        if(KeyType.RANDOM.equals(pixKey.getType())) {
            if (pixKey.getKey() == null || pixKey.getKey().isEmpty()) {
                return;
            }
            new IllegalArgumentException("The key must be null or empty");
        }

        KeyValidator<String> keyValidator = Optional.ofNullable(pixKey.getType())
                .orElseThrow(() -> new IllegalArgumentException("The key type cannot be null"))
                .getValidator();

        keyValidator.validate(pixKey.getKey());
    }
}
