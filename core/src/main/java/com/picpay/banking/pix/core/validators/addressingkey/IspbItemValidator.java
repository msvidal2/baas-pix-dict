package com.picpay.banking.pix.core.validators.addressingkey;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.validators.DictItemValidator;

public class IspbItemValidator implements DictItemValidator<AddressingKey> {

    @Override
    public void validate(AddressingKey addressingKey) {
        if (addressingKey.getIspb() == null) {
            throw new IllegalArgumentException("ispb can not be empty");
        }
        if (String.valueOf(addressingKey.getIspb()).length() > 8) {
            throw new IllegalArgumentException("The number of characters in the idpb must be less than 8");
        }
    }
}
