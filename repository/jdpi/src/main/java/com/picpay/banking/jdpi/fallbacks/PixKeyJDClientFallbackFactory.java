package com.picpay.banking.jdpi.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PixKeyJDClientFallbackFactory implements FallbackFactory<PixKeyJDClientFallback> {

    @Override
    public PixKeyJDClientFallback create(Throwable cause) {
        return new PixKeyJDClientFallback(cause);
    }

}
