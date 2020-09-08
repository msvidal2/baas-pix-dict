package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.AddressingKey;

import java.util.Collection;

public interface ListAddressingKeyPort {

    Collection<AddressingKey> listAddressingKey(final String requestIdentifier, final AddressingKey addressingKey);
}
