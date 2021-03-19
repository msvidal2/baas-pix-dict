package com.picpay.banking.pix.core.exception;

import lombok.Getter;

@Getter
public class PixKeyException extends UseCaseException {

    private PixKeyError pixKeyError;

    public PixKeyException(PixKeyError pixKeyError) {
        this.pixKeyError = pixKeyError;
    }

    public PixKeyException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return pixKeyError.getMessage();
    }

    @Override
    public String getCode() {
        return pixKeyError.getCode();
    }

}
