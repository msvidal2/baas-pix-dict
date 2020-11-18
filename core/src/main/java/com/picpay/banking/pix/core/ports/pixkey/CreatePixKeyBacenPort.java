package com.picpay.banking.pix.core.ports.pixkey;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;

public interface CreatePixKeyBacenPort {

    PixKey create(String requestIdentifier, PixKey pixKey, CreateReason reason);

}
