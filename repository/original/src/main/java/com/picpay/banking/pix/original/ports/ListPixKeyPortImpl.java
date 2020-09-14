package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.ListPixKeyPort;

import java.util.Collection;

public class ListPixKeyPortImpl implements ListPixKeyPort {
    @Override
    public Collection<PixKey> listPixKey(String requestIdentifier, PixKey pixKey) {
        throw new UnsupportedOperationException();
    }
}
