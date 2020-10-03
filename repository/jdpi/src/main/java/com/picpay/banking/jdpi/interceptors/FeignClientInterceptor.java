package com.picpay.banking.jdpi.interceptors;

import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.fallbacks.TokenManagerFallback;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

public class FeignClientInterceptor implements RequestInterceptor {

    private final static String CIRCUIT_BREAKER_NAME = "token-manager-feign-interceptor";

    private final String TOKEN_MANAGER_CLIENT = "tokenManagerClient";

    private TokenManagerClient tokenManagerClient;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Value("${pix.services.jdpi.headers.host}")
    private String host;

    public FeignClientInterceptor(TokenManagerClient tokenManagerClient, TimeLimiterExecutor timeLimiterExecutor) {
        this.tokenManagerClient = tokenManagerClient;
        this.timeLimiterExecutor = timeLimiterExecutor;
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "applyFallback")
    public void apply(RequestTemplate requestTemplate) {
        if (TOKEN_MANAGER_CLIENT.equalsIgnoreCase(requestTemplate.feignTarget().name())) {
            return;
        }

        var token = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> tokenManagerClient.getToken(TokenScope.DICT));

        requestTemplate.header(HttpHeaders.AUTHORIZATION, token.getTokenType() + " "+ token.getAccessToken());
    }

    public void applyFallback(RequestTemplate requestTemplate, Exception e) {
        new TokenManagerFallback(e).getToken(null);
    }

}
