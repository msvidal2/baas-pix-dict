package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface ClaimTopicBinding {

    String OUTPUT = "claim-notifications";

    @Output(OUTPUT)
    MessageChannel getClaimNotificationsOutput();
}
