package com.picpay.banking.reconciliation.config;

import com.picpay.banking.pixkey.config.DictEventOutputBinding;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(value = {DictEventOutputBinding.class})
public class PixKeyEventBindingConfig {

}
