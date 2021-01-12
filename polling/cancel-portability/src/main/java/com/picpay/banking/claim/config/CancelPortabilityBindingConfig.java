package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@EnableBinding(value = CancelPortabilityPollingOutputBinding.class)
@Configuration
public class CancelPortabilityBindingConfig {
}
