package com.picpay.banking.jdpi.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class TokenManagerFallbackFactory implements FallbackFactory<TokenManagerFallback> {

    @Override
    public TokenManagerFallback create(Throwable throwable) {
        return new TokenManagerFallback(throwable);
    }

}
