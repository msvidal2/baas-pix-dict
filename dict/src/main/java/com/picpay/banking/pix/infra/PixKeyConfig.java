package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pixkey.ports.CreatePixKeyPortImpl;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
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
    public CreatePixKeyPort createPixKeyPort(PixKeyRepository pixKeyRepository) {
        return new CreatePixKeyPortImpl(pixKeyRepository);
    }

}
