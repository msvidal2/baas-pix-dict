package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.CreateReason;

public interface CreateAddressingKeyPort {

    AddressingKey createAddressingKey(
            final AddressingKey addressingKey, final CreateReason reason, final String requestIdentifier);
}
