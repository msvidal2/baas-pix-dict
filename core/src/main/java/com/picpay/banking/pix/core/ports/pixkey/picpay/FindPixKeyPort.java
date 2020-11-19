package com.picpay.banking.pix.core.ports.pixkey.picpay;


import com.picpay.banking.pix.core.domain.PixKey;

public interface FindPixKeyPort {

    PixKey findPixKey(String pixKey);

}
