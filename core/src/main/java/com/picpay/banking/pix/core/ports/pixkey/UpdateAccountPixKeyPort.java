package com.picpay.banking.pix.core.ports.pixkey;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;

public interface UpdateAccountPixKeyPort {

    PixKey updateAccount(String requestIdentifier, PixKey pixKey, UpdateReason reason);
}
