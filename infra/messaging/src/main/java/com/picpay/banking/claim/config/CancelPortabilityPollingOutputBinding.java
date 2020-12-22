package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CancelPortabilityPollingOutputBinding {

    String OUTPUT = "cancel-portabilities-output-stream";

    @Output(OUTPUT)
    MessageChannel sendNewPortabilityToCancel();

}
