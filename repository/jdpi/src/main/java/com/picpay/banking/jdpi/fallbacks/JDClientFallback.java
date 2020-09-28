package com.picpay.banking.jdpi.fallbacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import com.picpay.banking.jdpi.exception.JDClientException;
import com.picpay.banking.jdpi.exception.TokenException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
public abstract class JDClientFallback {

    private Throwable cause;

    public JDClientFallback(Throwable cause) {
        this.cause = cause;
    }

    protected JDClientException resolveException() {
        if(cause instanceof FeignException) {
            return feignExceptionResolver((FeignException) cause);

        } else if(cause instanceof HystrixRuntimeException) {
            var hystrixCause = ((HystrixRuntimeException) cause)
                .getFallbackException()
                .getCause()
                .getCause();

            if(hystrixCause instanceof TokenException) {
                return tokenManagerExceptionResolver((TokenException) hystrixCause);
            }
        }

        return new JDClientException(cause.getMessage(), cause, null, BAD_GATEWAY);
    }

    private JDClientException feignExceptionResolver(FeignException feignException) {
        var error = parseError(feignException.responseBody().get().array());

        switch (HttpStatus.resolve(feignException.status())) {
            case NOT_FOUND:
                return new JDClientException("Not found", cause, error, NOT_FOUND);
            case BAD_REQUEST:
                return new JDClientException("Bad Request", cause, error, BAD_REQUEST);
        }

        return new JDClientException(cause.getMessage(), cause, null, BAD_GATEWAY);
    }

    private JDClientException tokenManagerExceptionResolver(TokenException hystrixCause) {
        var tokenException = hystrixCause;

        log.error(tokenException.getMessage(), tokenException);

        return new JDClientException("Connection error", tokenException, null, BAD_GATEWAY);
    }

    protected JDErrorDTO parseError(byte[] bytes) {
        if(bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return new ObjectMapper().readValue(bytes, JDErrorDTO.class);
        } catch (IOException ioException) {
            throw new JDClientException("An error has occurred when parse the feign content error", ioException, null, INTERNAL_SERVER_ERROR);
        }
    }

}
