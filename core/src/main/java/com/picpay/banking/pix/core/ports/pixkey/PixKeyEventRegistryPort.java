package com.picpay.banking.pix.core.ports.pixkey;

import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;

public interface PixKeyEventRegistryPort {

    void registry(EventType event, String requestIdentifier, PixKeyEventData key);

}
