package com.picpay.banking.pix.core.validators.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.validators.*;
import com.picpay.banking.pix.core.validators.key.KeyValidator;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class CreatePixKeyValidator {

    public static void validate(final String requestIdentifier, final PixKey pixKey, final Reason reason) {
        RequestIdentifierValidator.validate(requestIdentifier);

        if(Objects.isNull(pixKey)) {
            throw new IllegalArgumentException("Pix key cannot be null");
        }

        if(Objects.isNull(pixKey.getType())) {
            throw new IllegalArgumentException("Key type cannot be null");
        }

        if(!KeyType.RANDOM.equals(pixKey.getType())) { // TODO: Alterar teste para essa regra
            KeyValidator<String> keyValidator = pixKey.getType().getValidator();
            keyValidator.validate(pixKey.getKey());
        }

        AccountOpeningDateValidator.validate(pixKey.getAccountOpeningDate());
        AccountTypeValidator.validate(pixKey.getAccountType());
        AccountNumberValidator.validate(pixKey.getAccountNumber());
        BranchNumberValidator.validate(pixKey.getBranchNumber());
        TaxIdValidator.validate(pixKey.getTaxId());
        IspbValidator.validate(pixKey.getIspb());
        PersonTypeValidator.validate(pixKey.getPersonType());
        NameValidator.validate(pixKey.getName());

        if(Objects.isNull(reason)) {
            throw new IllegalArgumentException("Reason cannot be null");
        }
    }

}
