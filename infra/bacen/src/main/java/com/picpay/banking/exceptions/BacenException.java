package com.picpay.banking.exceptions;

import com.picpay.banking.fallbacks.BacenError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BacenException extends RuntimeException {

    private BacenError bacenError;

    private HttpStatus httpStatus;

    private boolean isRetryable;

    public boolean isRetryable() {
        if (bacenError != null) {
            return bacenError.isRetryable();
        }
        return isRetryable;
    }

    public BacenException(String message, HttpStatus status) {
        this(message, status, true);
    }

    public BacenException(String message, HttpStatus status, boolean isRetryable) {
        super(message);
        this.isRetryable = isRetryable;
        this.httpStatus = status;
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
