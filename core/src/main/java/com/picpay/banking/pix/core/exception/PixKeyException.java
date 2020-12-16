package com.picpay.banking.pix.core.exception;

import lombok.Getter;

@Getter
public class PixKeyException extends UseCaseException {

    private PixKeyError pixKeyError;

    public PixKeyException(PixKeyError pixKeyError) {
        this.pixKeyError = pixKeyError;
    }

    public PixKeyException(String message, PixKeyError pixKeyError) {
        super(message);
        this.pixKeyError = pixKeyError;
    }

    public PixKeyException(String message) {
        super(message);
    }

}
