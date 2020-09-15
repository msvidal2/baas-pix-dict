package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.FindPixKeyPort;

public class FindPixKeyPortImpl  implements FindPixKeyPort {
    @Override
    public PixKey findPixKey(PixKey pixKey, String userId) {
        throw new UnsupportedOperationException();
    }
}
