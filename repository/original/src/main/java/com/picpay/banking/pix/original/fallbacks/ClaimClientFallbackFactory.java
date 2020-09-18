package com.picpay.banking.pix.original.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ClaimClientFallbackFactory implements FallbackFactory<ClaimClientFallback> {

    @Override
    public ClaimClientFallback create(Throwable throwable) {
        return new ClaimClientFallback(throwable);
    }

}
