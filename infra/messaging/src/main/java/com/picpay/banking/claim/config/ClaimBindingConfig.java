package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(value = {ClaimNotificationOutputBinding.class, ClaimTopicBindingOutput.class, CancelPortabilityPollingOutputBinding.class})
public class ClaimBindingConfig {

}
