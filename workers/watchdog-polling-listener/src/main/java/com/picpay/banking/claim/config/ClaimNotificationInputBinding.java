package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ClaimNotificationInputBinding {

    String INPUT = "claim-notifications-consumer";

    @Input(INPUT)
    SubscribableChannel receiveNewClaimNotification();

}
