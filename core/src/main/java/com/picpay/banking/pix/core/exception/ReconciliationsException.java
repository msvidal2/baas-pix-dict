package com.picpay.banking.pix.core.exception;

import lombok.Getter;

@Getter
public class ReconciliationsException extends UseCaseException {

    public ReconciliationsException(final String message) {
        super(message);
    }

}
