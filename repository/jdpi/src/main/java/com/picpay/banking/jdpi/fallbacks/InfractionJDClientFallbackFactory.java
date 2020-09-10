package com.picpay.banking.jdpi.fallbacks;

import com.picpay.banking.jdpi.clients.InfractionJDClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class InfractionJDClientFallbackFactory implements FallbackFactory<InfractionJDClient> {

    @Override
    public InfractionJDClientFallback create(Throwable cause) {
        return new InfractionJDClientFallback(cause);
    }

}
