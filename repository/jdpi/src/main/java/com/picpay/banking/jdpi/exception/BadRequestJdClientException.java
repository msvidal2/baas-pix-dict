package com.picpay.banking.jdpi.exception;

import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import org.springframework.http.HttpStatus;

public class BadRequestJdClientException extends JDClientException {

    public BadRequestJdClientException(HttpStatus status) {
        super(status);
    }

    public BadRequestJdClientException(JDErrorDTO error, HttpStatus status) {
        super(error, status);
    }

    public BadRequestJdClientException(String message, HttpStatus status) {
        super(message, status);
    }

    public BadRequestJdClientException(String message, JDErrorDTO error, HttpStatus status) {
        super(message, error, status);
    }

    public BadRequestJdClientException(String message, Throwable cause, JDErrorDTO error, HttpStatus status) {
        super(message, cause, error, status);
    }

}
