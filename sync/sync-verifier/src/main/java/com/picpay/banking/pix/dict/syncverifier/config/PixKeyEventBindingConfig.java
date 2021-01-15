package com.picpay.banking.pix.dict.syncverifier.config;

import com.picpay.banking.pixkey.config.PixKeyEventOutputBinding;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(value = {PixKeyEventOutputBinding.class})
public class PixKeyEventBindingConfig {

}
