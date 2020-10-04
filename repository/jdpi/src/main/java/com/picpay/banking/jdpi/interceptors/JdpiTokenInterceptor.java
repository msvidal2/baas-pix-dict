package com.picpay.banking.jdpi.interceptors;

import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.fallbacks.TokenManagerFallback;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JdpiTokenInterceptor implements ClientHttpRequestInterceptor {

    private final static String CIRCUIT_BREAKER_NAME = "token-manager-feign-interceptor";

    @Value("pix.services.baas.token-manager.url")
    private String tokenManagerPath;

    @Value("${pix.services.jdpi.headers.host}")
    private String host;

    private TokenManagerClient tokenManagerClient;

    private TimeLimiterExecutor timeLimiterExecutor;

    public JdpiTokenInterceptor(TokenManagerClient tokenManagerClient, TimeLimiterExecutor timeLimiterExecutor) {
        this.tokenManagerClient = tokenManagerClient;
        this.timeLimiterExecutor = timeLimiterExecutor;
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "interceptFallback")
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        System.out.println("Host: " + request.getURI().getHost());

        if (tokenManagerPath.equalsIgnoreCase(request.getURI().getHost())) {
            return execution.execute(request, body);
        }

        var headers = request.getHeaders();

        var token = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> tokenManagerClient.getToken(TokenScope.DICT), "get-token-rest-template");

        headers.add(HttpHeaders.AUTHORIZATION, token.getTokenType() + " "+ token.getAccessToken());
        headers.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
        headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");

        return execution.execute(request, body);
    }

    public ClientHttpResponse interceptFallback(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, Exception e) throws IOException {
        new TokenManagerFallback(e).getToken(null);
        return null;
    }

}
