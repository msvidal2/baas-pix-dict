package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.CreateReason;

public interface CreatePixKeyPort {

    PixKey createPixKey(
            final PixKey pixKey, final CreateReason reason, final String requestIdentifier);
}
