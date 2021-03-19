package com.picpay.banking.pix.core.ports.pixkey.bacen;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;

public interface RemovePixKeyBacenPort {

    PixKeyEventData remove(PixKey pixKey, String requestIdentifier, Reason reason);

}
