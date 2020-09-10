package com.picpay.banking.pix.core.validators.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class IspbItemValidator implements DictItemValidator<PixKey> {

    @Override
    public void validate(PixKey pixKey) {
        if (pixKey.getIspb() == null) {
            throw new IllegalArgumentException("ispb can not be empty");
        }
        if (String.valueOf(pixKey.getIspb()).length() > 8) {
            throw new IllegalArgumentException("The number of characters in the idpb must be less than 8");
        }
    }
}
