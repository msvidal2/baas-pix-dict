package com.picpay.banking.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(ClaimTopicBinding.class)
public class KafkaConfig {

}
