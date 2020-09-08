package com.picpay.banking.jdpi.exception;

import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import org.springframework.http.HttpStatus;

public class NotFoundJdClientException extends JDClientException {

    public NotFoundJdClientException(HttpStatus status) {
        super(status);
    }

    public NotFoundJdClientException(JDErrorDTO error, HttpStatus status) {
        super(error, status);
    }

    public NotFoundJdClientException(String message, HttpStatus status) {
        super(message, status);
    }

    public NotFoundJdClientException(String message, JDErrorDTO error, HttpStatus status) {
        super(message, error, status);
    }

    public NotFoundJdClientException(String message, Throwable cause, JDErrorDTO error, HttpStatus status) {
        super(message, cause, error, status);
    }

}
