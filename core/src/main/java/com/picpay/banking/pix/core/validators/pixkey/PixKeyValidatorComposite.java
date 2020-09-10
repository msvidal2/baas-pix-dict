package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

import java.util.List;

public class PixKeyValidatorComposite implements DictItemValidator<PixKey> {

    private final List<DictItemValidator> validators;

    public PixKeyValidatorComposite(List<DictItemValidator> validators) {
        this.validators = validators;
    }

    @Override
    public void validate(PixKey pixKey) {
        validators.forEach(validators -> validators.validate(pixKey));
    }
}
