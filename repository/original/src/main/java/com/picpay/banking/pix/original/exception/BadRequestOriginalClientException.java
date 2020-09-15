package com.picpay.banking.pix.original.exception;

import com.picpay.banking.pix.original.dto.response.OriginalErrorDTO;
import org.springframework.http.HttpStatus;

public class BadRequestOriginalClientException extends OriginalClientException {

    public BadRequestOriginalClientException(HttpStatus status) {
        super(status);
    }

    public BadRequestOriginalClientException(OriginalErrorDTO error, HttpStatus status) {
        super(error, status);
    }

    public BadRequestOriginalClientException(String message, HttpStatus status) {
        super(message, status);
    }

    public BadRequestOriginalClientException(String message, OriginalErrorDTO error, HttpStatus status) {
        super(message, error, status);
    }

    public BadRequestOriginalClientException(String message, Throwable cause, OriginalErrorDTO error, HttpStatus status) {
        super(message, cause, error, status);
    }

}
