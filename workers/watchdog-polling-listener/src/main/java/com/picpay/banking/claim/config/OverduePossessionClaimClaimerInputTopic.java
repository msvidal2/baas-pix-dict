package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface OverduePossessionClaimClaimerInputTopic {

    String INPUT = "overdue-possession-claim-claimer-consumer";

    @Input(INPUT)
    SubscribableChannel getSubscribableChannel();

}
