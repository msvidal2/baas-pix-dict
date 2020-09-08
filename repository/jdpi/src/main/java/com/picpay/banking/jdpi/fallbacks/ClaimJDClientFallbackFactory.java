package com.picpay.banking.jdpi.fallbacks;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClaimJDClientFallbackFactory implements FallbackFactory<ClaimJDClientFallback> {

    @Override
    public ClaimJDClientFallback create(Throwable cause) {
        return new ClaimJDClientFallback(cause);
    }

}
