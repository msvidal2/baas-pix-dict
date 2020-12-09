package com.picpay.banking.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AcknowledgeInputStream {

    @Input("new-infraction-reports")
    SubscribableChannel receiveNewInfraction();

}
