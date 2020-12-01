package com.picpay.banking.pix.core.ports.pixkey.bacen;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;

public interface RemovePixKeyBacenPort {

    PixKey remove(PixKey pixKey, RemoveReason reason);

}
