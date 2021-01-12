package com.picpay.banking.pix.core.validators;


import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.key.KeyValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyItemValidator {

    public static void validate(PixKey pixKey) {
        if(KeyType.RANDOM.equals(pixKey.getType())) {
            if (pixKey.getKey() == null || pixKey.getKey().isEmpty()) {
                return;
            }

            throw new IllegalArgumentException("Key must be null or empty");
        }

        KeyValidator<String> keyValidator = Optional.ofNullable(pixKey.getType())
                .orElseThrow(() -> new IllegalArgumentException("Key type cannot be null"))
                .getValidator();

        keyValidator.validate(pixKey.getKey());
    }
}
