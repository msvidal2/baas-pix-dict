package com.picpay.banking.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ClaimTopicBinding {

    String OUTPUT = "claim-notifications";

    @Output(OUTPUT)
    MessageChannel getClaimNotificationsOutput();
}
