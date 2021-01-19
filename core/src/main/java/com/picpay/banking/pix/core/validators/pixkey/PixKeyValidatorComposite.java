package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PixKeyValidatorComposite implements DictItemValidator<PixKey> {

    private final Collection<DictItemValidator> validators;

    public PixKeyValidatorComposite(Collection<DictItemValidator> validators) {
        this.validators = Collections.unmodifiableCollection(validators);
    }

    @Override
    public void validate(PixKey pixKey) {
        validators.forEach(validators -> validators.validate(pixKey));
    }
}
