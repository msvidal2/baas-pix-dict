package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface OverduePossessionClaimInputTopic {

    String INPUT = "overdue-possession-claim-consumer";

    @Input(INPUT)
    SubscribableChannel getSubscribableChannel();

}
