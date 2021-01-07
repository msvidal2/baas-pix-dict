package com.picpay.banking.reconciliation.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(value = {ReconciliationSyncEventOutputBinding.class, ReconciliationEventsInputBinding.class})
public class ReconciliationEventsBindingConfig {

}
