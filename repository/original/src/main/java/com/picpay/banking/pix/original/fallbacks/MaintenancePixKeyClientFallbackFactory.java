package com.picpay.banking.pix.original.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MaintenancePixKeyClientFallbackFactory implements FallbackFactory<MaintenancePixKeyClientFallback> {

    @Override
    public MaintenancePixKeyClientFallback create(Throwable throwable) {
        return new MaintenancePixKeyClientFallback(throwable);
    }

}
