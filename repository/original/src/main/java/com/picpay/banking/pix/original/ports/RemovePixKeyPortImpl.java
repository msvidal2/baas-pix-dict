package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.RemovePixKeyPort;

public class RemovePixKeyPortImpl implements RemovePixKeyPort {
    @Override
    public void remove(PixKey pixKey, RemoveReason reason, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }
}
