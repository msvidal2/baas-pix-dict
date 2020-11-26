package com.picpay.banking.pix.core.ports.pixkey.picpay;


import com.picpay.banking.pix.core.domain.PixKey;

import java.util.Collection;

public interface ListPixKeyPort {

    Collection<PixKey> listPixKey(String requestIdentifier, PixKey pixKey);
}
