package com.picpay.banking.pix.core.validators.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ClaimValidatorComposite implements DictItemValidator<Claim> {

    private final Collection<DictItemValidator> validators;

    public ClaimValidatorComposite(Collection<DictItemValidator> validators) {
        this.validators = validators;
    }

    @Override
    public void validate(Claim claim) {
        validators.forEach(validators -> validators.validate(claim));
    }
}
