package com.picpay.banking.pix.core.ports.pixkey.picpay;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;

public interface RemovePixKeyPort {

    PixKey remove(String requestIdentifier, PixKey pixKey, RemoveReason reason);

}
