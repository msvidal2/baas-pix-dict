package com.picpay.banking.pixkey.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface PixKeyEventOutputBinding {

    String TOPIC = "keys-changed";

    @Output(TOPIC)
    MessageChannel sendPixKeyWasChanged();

}
