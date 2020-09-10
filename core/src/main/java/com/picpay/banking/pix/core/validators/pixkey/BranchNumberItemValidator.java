package com.picpay.banking.pix.core.validators.pixkey;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class BranchNumberItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey pixKey) {
        var branchNumber = pixKey.getBranchNumber();

        if (branchNumber == null) {
            return;
        }

        if (Strings.isNullOrEmpty(branchNumber)) {
            throw new IllegalArgumentException("branchNumber can not be empty");
        }

        if (branchNumber.trim().length() != 4) {
            throw new IllegalArgumentException("The number of characters in the branchNumber must be 4");
        }
    }

}
