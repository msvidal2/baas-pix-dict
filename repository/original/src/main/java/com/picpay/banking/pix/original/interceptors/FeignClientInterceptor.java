package com.picpay.banking.pix.original.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Value("${pix.services.original.api-key}")
    private String apiKey;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(AUTHORIZATION, apiKey);
    }

}
