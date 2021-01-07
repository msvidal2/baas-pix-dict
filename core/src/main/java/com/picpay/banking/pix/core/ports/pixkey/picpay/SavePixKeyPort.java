package com.picpay.banking.pix.core.ports.pixkey.picpay;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;

public interface SavePixKeyPort {

    PixKey savePixKey(PixKey pixKey, Reason reason);

}
