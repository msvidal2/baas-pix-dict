package com.picpay.banking.reconciliation.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

public interface ReconciliationEventsInputBinding {

    String INPUT = "reconciliation-cid";

    @Input(INPUT)
    SubscribableChannel receiveReconciliationEvent();

}
