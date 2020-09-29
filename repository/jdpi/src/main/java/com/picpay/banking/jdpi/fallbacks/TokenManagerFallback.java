package com.picpay.banking.jdpi.fallbacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.dto.response.TokenDTO;
import com.picpay.banking.jdpi.dto.response.TokenErrorDTO;
import com.picpay.banking.jdpi.exception.TokenException;
import com.picpay.banking.jdpi.interceptors.TokenScope;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class TokenManagerFallback implements TokenManagerClient {

    private final Throwable cause;

    public TokenManagerFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public TokenDTO getToken(TokenScope scope) {
        log.error("Error to get a token: {}", cause.getMessage());

        if(cause instanceof FeignException) {
            var exception = (FeignException) cause;

            if(exception.status() == -1) {
                throw new TokenException(cause.getMessage(), cause);
            }

            try {
                var error = new ObjectMapper().readValue(exception.responseBody().get().array(), TokenErrorDTO.class);

                throw new TokenException(error.getMessage(), exception);
            } catch (IOException e) {
                log.error("Error when parse response", e);
                throw new TokenException(e.getMessage(), e);
            }
        }

        throw new TokenException(cause.getMessage(), cause);
    }

}
