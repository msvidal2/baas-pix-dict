package com.picpay.banking.pix.core.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.events.PixKeyEvent;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;

public interface PixKeyEventRegistryPort {

    void registry(PixKeyEvent event, String requestIdentifier, PixKeyEventData key, Reason reason);

}
