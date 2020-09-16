package com.picpay.banking.pix.core.ports.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;

public interface FindPixKeyPort {

    PixKey findPixKey(String requestIdentifier, PixKey pixKey, String userId);
}
