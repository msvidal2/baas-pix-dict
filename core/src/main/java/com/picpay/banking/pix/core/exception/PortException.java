package com.picpay.banking.pix.core.exception;

public class PortException extends RuntimeException {

    public PortException() {
    }

    public PortException(String message) {
        super(message);
    }

    public PortException(String message, Throwable cause) {
        super(message, cause);
    }

    public PortException(Throwable cause) {
        super(cause);
    }

    public PortException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
