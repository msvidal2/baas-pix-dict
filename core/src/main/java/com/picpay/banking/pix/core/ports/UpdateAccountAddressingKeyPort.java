package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.UpdateReason;

public interface UpdateAccountAddressingKeyPort {

    AddressingKey updateAccount(AddressingKey addressingKey, UpdateReason reason, String requestIdentifier);
}
