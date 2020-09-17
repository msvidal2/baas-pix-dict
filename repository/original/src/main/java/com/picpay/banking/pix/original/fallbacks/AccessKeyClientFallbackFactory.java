package com.picpay.banking.pix.original.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AccessKeyClientFallbackFactory implements FallbackFactory<AccessKeyClientFallback> {

    @Override
    public AccessKeyClientFallback create(Throwable throwable) {
        return new AccessKeyClientFallback(throwable);
    }

}
