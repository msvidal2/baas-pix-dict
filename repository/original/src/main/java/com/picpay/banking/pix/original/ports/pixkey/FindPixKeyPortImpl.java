package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;

public class FindPixKeyPortImpl  implements FindPixKeyPort {

    @Override
    public PixKey findPixKey(String requestIdentifier, PixKey pixKey, String userId) {
        throw new UnsupportedOperationException();
    }
}
