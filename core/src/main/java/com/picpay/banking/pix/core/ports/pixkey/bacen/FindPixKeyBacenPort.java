package com.picpay.banking.pix.core.ports.pixkey.bacen;

import com.picpay.banking.pix.core.domain.PixKey;

public interface FindPixKeyBacenPort {

    PixKey findPixKey(String requestIdentifier, String pixKey, String userId);

}
