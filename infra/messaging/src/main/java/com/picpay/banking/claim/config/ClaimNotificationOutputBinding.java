package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ClaimNotificationOutputBinding {

    String OUTPUT = "send-new-claim-notification";

    @Output(OUTPUT)
    MessageChannel sendNewClaimNotification();

}
