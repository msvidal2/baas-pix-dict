package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CancelPortabilityPollingInputBinding {

    String INPUT = "cancel-portabilities-input-stream";

    @Input(INPUT)
    SubscribableChannel receiveNewPortabilityToCancel();

}
