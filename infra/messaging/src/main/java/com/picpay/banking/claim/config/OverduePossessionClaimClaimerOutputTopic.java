package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OverduePossessionClaimClaimerOutputTopic {

    String OUTPUT = "overdue-possession-claim";

    @Output(OUTPUT)
    MessageChannel getMessageChannel();

}
