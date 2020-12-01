package com.picpay.banking.pix.core.ports.pixkey.bacen;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;

public interface UpdateAccountPixKeyBacenPort {

    PixKey update(String requestIdentifier, PixKey pixKey, UpdateReason reason);

}
