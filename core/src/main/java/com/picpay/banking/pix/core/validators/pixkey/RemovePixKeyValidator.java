package com.picpay.banking.pix.core.validators.pixkey;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.validators.IspbValidator;
import com.picpay.banking.pix.core.validators.key.KeyValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RemovePixKeyValidator {

    public static void validate(final String requestIdentifier,
                                final PixKey pixKey,
                                final Reason reason) {
        if (Strings.isNullOrEmpty(requestIdentifier) || requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("RequestIdentifier cannot be empty");
        }

        if(Objects.isNull(pixKey)) {
            throw new IllegalArgumentException("Pix key cannot be null");
        }

        if(Objects.isNull(pixKey.getType())) {
            throw new IllegalArgumentException("Key type cannot be null");
        }

        KeyValidator<String> keyValidator = pixKey.getType().getValidator();
        keyValidator.validate(pixKey.getKey());

        IspbValidator.validate(pixKey.getIspb());

        if(Objects.isNull(reason)) {
            throw new IllegalArgumentException("Reason cannot be null");
        }
    }
}
