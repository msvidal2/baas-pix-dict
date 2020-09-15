package com.picpay.banking.pix.original.fallbacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.original.dto.response.OriginalErrorDTO;
import com.picpay.banking.pix.original.exception.NotFoundOriginalClientException;
import com.picpay.banking.pix.original.exception.OriginalClientException;
import feign.FeignException;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public abstract class OriginalClientFallback {

    private Throwable cause;

    public OriginalClientFallback(Throwable cause) {
        this.cause = cause;
    }

    protected OriginalClientException resolveException() {
        if(cause instanceof FeignException) {
            var feignException = (FeignException) cause;
            var error = parseError(feignException.responseBody().get().array());

            switch (HttpStatus.resolve(feignException.status())) {
                case NOT_FOUND:
                    return new NotFoundOriginalClientException("Not found", cause, error, NOT_FOUND);
                case BAD_REQUEST:
                    return new OriginalClientException("Bad Request", cause, error, BAD_REQUEST);
            }
        }

        return new OriginalClientException(cause.getMessage(), cause, null, INTERNAL_SERVER_ERROR);
    }

    protected OriginalErrorDTO parseError(byte[] bytes) {
        if(bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return new ObjectMapper().readValue(bytes, OriginalErrorDTO.class);
        } catch (IOException ioException) {
            throw new OriginalClientException("An error has occurred when parse the feign content error", ioException, null, INTERNAL_SERVER_ERROR);
        }
    }

}
