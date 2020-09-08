package com.picpay.banking.jdpi.exception;

import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class JDClientException extends RuntimeException {

    private JDErrorDTO error;

    private HttpStatus status;

    public JDClientException(HttpStatus status) {
        this.status = status;
    }

    public JDClientException(JDErrorDTO error, HttpStatus status) {
        this.error = error;
        this.status = status;
    }

    public JDClientException(String message, HttpStatus status) {
        this(message, null, status);
    }

    public JDClientException(String message, JDErrorDTO error, HttpStatus status) {
        this(message, null, error, status);
    }

    public JDClientException(String message, Throwable cause, JDErrorDTO error, HttpStatus status) {
        super(message, cause);
        this.error = error;
        this.status = status;
    }

    public Optional<JDErrorDTO> getError() {
        return Optional.ofNullable(error);
    }

    public HttpStatus getStatus() {
        return status;
    }

}
