package com.picpay.banking.pixkey.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DictEventInputBinding {

    String INPUT = "pixKey-events";

    @Input(INPUT)
    SubscribableChannel input();

}
