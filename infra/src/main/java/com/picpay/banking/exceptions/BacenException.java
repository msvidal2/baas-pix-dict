package com.picpay.banking.exceptions;

import com.picpay.banking.fallbacks.BacenError;
import org.springframework.http.HttpStatus;

public class BacenException extends RuntimeException {

    private BacenError bacenError;

    private HttpStatus httpStatus;

    public BacenException(String message, HttpStatus status) {
        super(message);
    }

    public BacenException(String message, BacenError bacenError, HttpStatus status) {
        this(message, bacenError, status, null);
    }

    public BacenException(String message, BacenError bacenError, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.bacenError = bacenError;
        this.httpStatus = status;
    }

}
