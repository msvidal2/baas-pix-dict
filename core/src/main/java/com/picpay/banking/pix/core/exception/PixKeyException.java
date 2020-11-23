package com.picpay.banking.pix.core.exception;

import com.picpay.banking.pix.core.domain.PixKeyError;
import lombok.Getter;

@Getter
public class PixKeyException extends UseCaseException {

    private PixKeyError pixKeyError;

    public PixKeyException(PixKeyError pixKeyError) {
        this.pixKeyError = pixKeyError;
    }

}
