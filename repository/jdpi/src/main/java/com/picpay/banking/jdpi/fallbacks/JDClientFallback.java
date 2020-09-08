package com.picpay.banking.jdpi.fallbacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import com.picpay.banking.jdpi.exception.JDClientException;
import com.picpay.banking.jdpi.exception.NotFoundJdClientException;
import feign.FeignException;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public abstract class JDClientFallback {

    private Throwable cause;

    public JDClientFallback(Throwable cause) {
        this.cause = cause;
    }

    protected JDClientException resolveException() {
        if(cause instanceof FeignException) {
            var feignException = (FeignException) cause;
            var error = parseError(feignException.responseBody().get().array());

            switch (HttpStatus.resolve(feignException.status())) {
                case NOT_FOUND:
                    return new NotFoundJdClientException("Not found", cause, error, NOT_FOUND);
                case BAD_REQUEST:
                    return new JDClientException("Bad Request", cause, error, BAD_REQUEST);
            }
        }

        return new JDClientException(cause.getMessage(), cause, null, INTERNAL_SERVER_ERROR);
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
