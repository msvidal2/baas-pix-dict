package com.picpay.banking.pix.core.validators.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListPixKeyValidator {

    public static void validate(String requestIdentifier, final PixKey pixKey) {
        RequestIdentifierValidator.validate(requestIdentifier);
        TaxIdValidator.validate(pixKey.getTaxId());
        PersonTypeValidator.validate(pixKey.getPersonType());
        BranchNumberValidator.validate(pixKey.getBranchNumber());
        AccountNumberValidator.validate(pixKey.getAccountNumber());
        AccountTypeValidator.validate(pixKey.getAccountType());
        IspbValidator.validate(pixKey.getIspb());
    }

}
