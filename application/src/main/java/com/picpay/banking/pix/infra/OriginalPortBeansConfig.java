package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.ListPixKeyPort;
import com.picpay.banking.pix.original.clients.SearchPixKeyClient;
import com.picpay.banking.pix.original.interceptors.FeignClientInterceptor;
import com.picpay.banking.pix.original.ports.ListPixKeyPortImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

//@ConditionalOnProperty(value = "pix.partner", havingValue = "original")
@Configuration
public class OriginalPortBeansConfig {

    @Bean
    @Primary
    public FeignClientInterceptor feignClientInterceptor(){
        return new FeignClientInterceptor();
    }

    @Bean
    @Primary
    public ListPixKeyPort listPixKeyPort(SearchPixKeyClient searchPixKeyClient) {
        return new ListPixKeyPortImpl(searchPixKeyClient);
    }

}
