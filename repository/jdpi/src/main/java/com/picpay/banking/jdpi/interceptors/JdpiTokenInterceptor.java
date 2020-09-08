package com.picpay.banking.jdpi.interceptors;

import com.picpay.banking.jdpi.clients.TokenManagerClient;
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

    @Value("pix.services.baas.token-manager.url")
    private String tokenManagerPath;

    @Value("${pix.services.jdpi.headers.host}")
    private String host;

    private TokenManagerClient tokenManagerClient;

    public JdpiTokenInterceptor(TokenManagerClient tokenManagerClient) {
        this.tokenManagerClient = tokenManagerClient;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        System.out.println("Host: " + request.getURI().getHost());

        if (tokenManagerPath.equalsIgnoreCase(request.getURI().getHost())) {
            return execution.execute(request, body);
        }

        var headers = request.getHeaders();

        var token = tokenManagerClient.getToken(TokenScope.DICT);

        headers.add(HttpHeaders.AUTHORIZATION, token.getTokenType() + " "+ token.getAccessToken());
        headers.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
        headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");
        headers.add(HttpHeaders.HOST, host);

        return execution.execute(request, body);
    }

}
