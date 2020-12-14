package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

public interface ClaimNotificationOutputBinding {

    String OUTPUT = "send-new-claim-notification";

    @Output(OUTPUT)
    MessageChannel sendNewClaimNotification();

}
