package com.picpay.banking.jdpi.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AddressingKeyJDClientFallbackFactory implements FallbackFactory<AddressingKeyJDClientFallback> {

    @Override
    public AddressingKeyJDClientFallback create(Throwable cause) {
        return new AddressingKeyJDClientFallback(cause);
    }

}
