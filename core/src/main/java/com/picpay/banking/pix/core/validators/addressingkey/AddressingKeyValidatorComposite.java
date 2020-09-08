package com.picpay.banking.pix.core.validators.addressingkey;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

import java.util.List;

public class AddressingKeyValidatorComposite implements DictItemValidator<AddressingKey> {

    private final List<DictItemValidator> validators;

    public AddressingKeyValidatorComposite(List<DictItemValidator> validators) {
        this.validators = validators;
    }

    @Override
    public void validate(AddressingKey addressingKey) {
        validators.forEach(validators -> validators.validate(addressingKey));
    }
}
