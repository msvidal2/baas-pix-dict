package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.AddressingKey;

public interface FindAddressingKeyPort {

    AddressingKey findAddressingKey(final AddressingKey addressingKey, final String userId);
}
