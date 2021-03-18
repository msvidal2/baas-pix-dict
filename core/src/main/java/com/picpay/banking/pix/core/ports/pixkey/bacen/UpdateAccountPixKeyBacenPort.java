package com.picpay.banking.pix.core.ports.pixkey.bacen;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.DomainEvent;

public interface UpdateAccountPixKeyBacenPort {

    DomainEvent<PixKey> update(String requestIdentifier, PixKey pixKey, Reason reason);

}
