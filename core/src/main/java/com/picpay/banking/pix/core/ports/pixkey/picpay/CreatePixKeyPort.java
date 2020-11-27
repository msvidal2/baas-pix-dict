package com.picpay.banking.pix.core.ports.pixkey.picpay;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.CreateReason;

public interface CreatePixKeyPort {

    PixKey createPixKey(PixKey pixKey, CreateReason reason);
}
