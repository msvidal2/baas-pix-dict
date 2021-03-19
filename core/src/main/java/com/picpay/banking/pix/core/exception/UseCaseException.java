package com.picpay.banking.pix.core.exception;

public abstract class UseCaseException extends RuntimeException {

    public UseCaseException() {
    }

    public UseCaseException(String message) {
        super(message);
    }

    public UseCaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public UseCaseException(Throwable cause) {
        super(cause);
    }

    public UseCaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public abstract String getCode();

    public abstract String getMessage();

}
