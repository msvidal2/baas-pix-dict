package com.picpay.banking.reconciliation.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface NotifyReconciliationStream {

    @Output("reconciliation-sync")
    MessageChannel notifyPixKeyChangeReconciliation();

}
