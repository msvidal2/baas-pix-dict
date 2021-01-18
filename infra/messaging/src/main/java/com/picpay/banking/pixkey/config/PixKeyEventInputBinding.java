package com.picpay.banking.pixkey.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface PixKeyEventInputBinding {

    String INPUT = "keys-changed";

    @Input(INPUT)
    SubscribableChannel receivePixKeyWasChanged();

}
