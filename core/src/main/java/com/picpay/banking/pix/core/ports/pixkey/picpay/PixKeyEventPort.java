package com.picpay.banking.pix.core.ports.pixkey.picpay;

import com.picpay.banking.pix.core.domain.PixKey;

public interface PixKeyEventPort {

    void pixKeyWasCreated(PixKey pixKey);

    void pixKeyWasUpdated(PixKey pixKey);

    void pixKeyWasRemoved(PixKey pixKey);

}
