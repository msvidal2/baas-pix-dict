package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.PixKey;

import java.util.Collection;

public interface ListPixKeyPort {

    Collection<PixKey> listAddressingKey(final String requestIdentifier, final PixKey pixKey);
}
