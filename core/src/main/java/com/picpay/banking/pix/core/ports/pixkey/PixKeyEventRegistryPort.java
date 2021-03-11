package com.picpay.banking.pix.core.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeyEvent;
import com.picpay.banking.pix.core.domain.Reason;

public interface PixKeyEventRegistryPort {

    void registry(PixKeyEvent event, String requestIdentifier, PixKey key, Reason reason);

}
