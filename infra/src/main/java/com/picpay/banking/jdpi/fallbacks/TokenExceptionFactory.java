package com.picpay.banking.jdpi.fallbacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.jdpi.dto.response.TokenDTO;
import com.picpay.banking.jdpi.dto.response.TokenErrorDTO;
import com.picpay.banking.jdpi.exception.TokenException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class TokenExceptionFactory {

    public static TokenException from(Exception cause) {
        log.error("Error to get a token: {}", cause.getMessage());

        if(cause instanceof FeignException) {
            var exception = (FeignException) cause;

            if(exception.status() == -1) {
                return new TokenException(cause.getMessage(), cause);
            }

            try {
                var error = new ObjectMapper().readValue(exception.responseBody().get().array(), TokenErrorDTO.class);

                return new TokenException(error.getMessage(), exception);
            } catch (IOException e) {
                log.error("Error when parse response", e);
                return new TokenException(e.getMessage(), e);
            }
        }

        return new TokenException(cause.getMessage(), cause);
    }

}
