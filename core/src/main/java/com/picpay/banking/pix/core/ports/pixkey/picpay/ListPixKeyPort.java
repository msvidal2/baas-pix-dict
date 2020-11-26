package com.picpay.banking.pix.core.ports.pixkey.picpay;


import com.picpay.banking.pix.core.domain.PixKey;

import java.util.List;

public interface ListPixKeyPort {

    List<PixKey> listPixKey(String requestIdentifier, PixKey pixKey);
}
