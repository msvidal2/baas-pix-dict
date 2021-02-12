package com.picpay.banking.reconciliation.config;

import com.picpay.banking.pixkey.config.PixKeyEventOutputBinding;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/**
 * @author Luis Silva
 * @version 1.0 21/01/2021
 */
@Configuration
@EnableBinding(PixKeyEventOutputBinding.class)
public class KafkaConfig {

}
