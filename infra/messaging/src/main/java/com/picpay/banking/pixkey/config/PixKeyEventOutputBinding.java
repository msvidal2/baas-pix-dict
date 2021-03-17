package com.picpay.banking.pixkey.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


public interface PixKeyEventOutputBinding {

    String OUTPUT = "pixKey-events";

    @Output(OUTPUT)
    MessageChannel output();

}
