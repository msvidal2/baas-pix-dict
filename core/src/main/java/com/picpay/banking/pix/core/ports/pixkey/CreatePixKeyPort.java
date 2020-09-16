package com.picpay.banking.pix.core.ports.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.CreateReason;

public interface CreatePixKeyPort {

    PixKey createPixKey(String requestIdentifier, PixKey pixKey, CreateReason reason);
}
