package com.picpay.banking.pix.dict.infraction.outputevent;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface NotifyOutputStream {

    @Output("pix-alert-notification")
    MessageChannel sendNotification();

}
