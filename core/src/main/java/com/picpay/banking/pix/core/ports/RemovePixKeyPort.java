package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;

public interface RemovePixKeyPort {

    PixKey remove(PixKey pixKey, RemoveReason reason, String requestIdentifier);

}
