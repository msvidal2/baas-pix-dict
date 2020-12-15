package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ClaimTopicBindingOutput {

    String OUTPUT = "claim-notifications";

    @Output(OUTPUT)
    MessageChannel getClaimNotificationsOutput();
}
