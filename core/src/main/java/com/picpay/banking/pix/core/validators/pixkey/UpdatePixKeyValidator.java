package com.picpay.banking.pix.core.validators.pixkey;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.validators.*;
import com.picpay.banking.pix.core.validators.key.KeyValidator;

import java.util.Objects;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 25/11/20
 */
public class UpdatePixKeyValidator {

    public static void validate(final String requestIdentifier, final PixKey pixKey, final UpdateReason reason) {
        if (Strings.isNullOrEmpty(requestIdentifier) || requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier cannot be empty");
        }

        if (Objects.isNull(pixKey)) {
            throw new IllegalArgumentException("Pix key cannot be null");
        }

        if (Objects.isNull(pixKey.getType())) {
            throw new IllegalArgumentException("Key type cannot be null");
        }

        KeyValidator<String> keyValidator = pixKey.getType().getValidator();
        keyValidator.validate(pixKey.getKey());

        AccountOpeningDateValidator.validate(pixKey.getAccountOpeningDate());
        AccountTypeValidator.validate(pixKey.getAccountType());
        AccountNumberValidator.validate(pixKey.getAccountNumber());
        BranchNumberValidator.validate(pixKey.getBranchNumber());
        IspbValidator.validate(pixKey.getIspb());

        if (Objects.isNull(reason)) {
            throw new IllegalArgumentException("Reason cannot be null");
        }
    }

}
