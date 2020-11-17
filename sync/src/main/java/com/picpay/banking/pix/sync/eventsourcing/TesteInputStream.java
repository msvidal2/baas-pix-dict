package com.picpay.banking.pix.sync.eventsourcing;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface TesteInputStream {

    @Input("sync-input")
    SubscribableChannel syncInput();

}
