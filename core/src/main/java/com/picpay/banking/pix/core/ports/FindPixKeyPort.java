package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.PixKey;

public interface FindPixKeyPort {

    PixKey findPixKey(final PixKey pixKey, final String userId);
}
