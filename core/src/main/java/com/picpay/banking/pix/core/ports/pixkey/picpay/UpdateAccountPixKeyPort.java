package com.picpay.banking.pix.core.ports.pixkey.picpay;


import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;

public interface UpdateAccountPixKeyPort {

    PixKey updateAccount(PixKey pixKey, UpdateReason reason);
}
