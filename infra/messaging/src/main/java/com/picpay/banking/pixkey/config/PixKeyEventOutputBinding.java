package com.picpay.banking.pixkey.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PixKeyEventOutputBinding {

    String OUTPUT = "keys-changed";

    @Output(OUTPUT)
    MessageChannel sendPixKeyWasChanged();

}
