package com.picpay.banking.jdpi.fallbacks;

import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class InfractionReportJDClientFallbackFactory implements FallbackFactory<InfractionReportJDClient> {

    @Override
    public InfractionReportJDClientFallback create(Throwable cause) {
        return new InfractionReportJDClientFallback(cause);
    }

}
