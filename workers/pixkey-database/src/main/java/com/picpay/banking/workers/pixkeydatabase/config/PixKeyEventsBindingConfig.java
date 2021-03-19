package com.picpay.banking.workers.pixkeydatabase.config;

import com.picpay.banking.pixkey.config.DictEventInputBinding;
import com.picpay.banking.pixkey.config.DictEventOutputBinding;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(value = {DictEventInputBinding.class, DictEventOutputBinding.class})
public class PixKeyEventsBindingConfig {

}
