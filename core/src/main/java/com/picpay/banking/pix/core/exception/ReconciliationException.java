package com.picpay.banking.pix.core.exception;

import lombok.Getter;

@Getter
public class ReconciliationException extends UseCaseException {

    public ReconciliationException(final String message) {
        super(message);
    }

    public ReconciliationException(final String message, final InterruptedException e) {
        super(message, e);
    }

}
