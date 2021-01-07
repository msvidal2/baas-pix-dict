package com.picpay.banking.reconciliation.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface ReconciliationSyncEventOutputBinding {

    @Output("new-reconciliation-cid")
    MessageChannel sendReconciliationSyncEvent();

}
