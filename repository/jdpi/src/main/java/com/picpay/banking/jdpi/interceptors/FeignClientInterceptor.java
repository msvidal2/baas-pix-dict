package com.picpay.banking.jdpi.interceptors;

import com.picpay.banking.jdpi.clients.TokenManagerClient;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private final String TOKEN_MANAGER_CLIENT = "tokenManagerClient";

    private TokenManagerClient tokenManagerClient;

    @Value("${pix.services.jdpi.headers.host}")
    private String host;

    public FeignClientInterceptor(TokenManagerClient tokenManagerClient) {
        this.tokenManagerClient = tokenManagerClient;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (TOKEN_MANAGER_CLIENT.equalsIgnoreCase(requestTemplate.feignTarget().name())) {
            return;
        }

        var token = tokenManagerClient.getToken(TokenScope.DICT);

        requestTemplate.header(HttpHeaders.AUTHORIZATION, token.getTokenType() + " "+ token.getAccessToken());
        requestTemplate.header(HttpHeaders.HOST, host);
    }

}
