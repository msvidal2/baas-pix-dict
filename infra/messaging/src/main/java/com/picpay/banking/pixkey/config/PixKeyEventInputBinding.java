package com.picpay.banking.pixkey.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PixKeyEventInputBinding {

    String INPUT = "keys-changed";

    @Input(INPUT)
    SubscribableChannel receivePixKeyWasChanged();

}
