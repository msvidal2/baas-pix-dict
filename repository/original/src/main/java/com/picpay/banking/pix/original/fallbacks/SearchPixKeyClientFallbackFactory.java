package com.picpay.banking.pix.original.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SearchPixKeyClientFallbackFactory implements FallbackFactory<SearchPixKeyClientFallback> {

    @Override
    public SearchPixKeyClientFallback create(Throwable throwable) {
        return new SearchPixKeyClientFallback(throwable);
    }

}
