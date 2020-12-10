package com.picpay.banking.infraction.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface InfractionAlertNotificationOutputStream {

    @Output("alert-notification-topic")
    MessageChannel sendAlertNotification();

}
