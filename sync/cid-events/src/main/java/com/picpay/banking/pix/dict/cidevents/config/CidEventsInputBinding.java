package com.picpay.banking.pix.dict.cidevents.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface CidEventsInputBinding {

    @Input("sync-input")
    SubscribableChannel incomingToProcess();

}
