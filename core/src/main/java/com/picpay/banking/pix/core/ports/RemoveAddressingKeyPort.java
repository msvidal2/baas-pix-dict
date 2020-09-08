package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.RemoveReason;

public interface RemoveAddressingKeyPort {

    void remove(AddressingKey addressingKey, RemoveReason reason, String requestIdentifier);

}
