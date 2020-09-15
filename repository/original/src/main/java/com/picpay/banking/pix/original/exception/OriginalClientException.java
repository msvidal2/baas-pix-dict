package com.picpay.banking.pix.original.exception;

import com.picpay.banking.pix.original.dto.response.OriginalErrorDTO;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class OriginalClientException extends RuntimeException {

    private OriginalErrorDTO error;

    private HttpStatus status;

    public OriginalClientException(HttpStatus status) {
        this.status = status;
    }

    public OriginalClientException(OriginalErrorDTO error, HttpStatus status) {
        this.error = error;
        this.status = status;
    }

    public OriginalClientException(String message, HttpStatus status) {
        this(message, null, status);
    }

    public OriginalClientException(String message, OriginalErrorDTO error, HttpStatus status) {
        this(message, null, error, status);
    }

    public OriginalClientException(String message, Throwable cause, OriginalErrorDTO error, HttpStatus status) {
        super(message, cause);
        this.error = error;
        this.status = status;
    }

    public Optional<OriginalErrorDTO> getError() {
        return Optional.ofNullable(error);
    }

    public HttpStatus getStatus() {
        return status;
    }

}
