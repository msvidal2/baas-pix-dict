package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import com.picpay.banking.pixkey.clients.BacenKeyClient;
import com.picpay.banking.pixkey.ports.CreatePixKeyPortImpl;
import com.picpay.banking.pixkey.ports.SavePixKeyPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 17/11/20
 */
@Configuration
public class PixKeyConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public CreatePixKeyPort createPixKeyPort(BacenKeyClient bacenKeyClient,
                                             SavePixKeyPort savePixKeyPort) {
        return new CreatePixKeyPortImpl(bacenKeyClient, savePixKeyPort);
    }

}
