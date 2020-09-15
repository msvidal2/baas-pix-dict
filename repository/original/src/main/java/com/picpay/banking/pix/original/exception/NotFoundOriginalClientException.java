package com.picpay.banking.pix.original.exception;

import com.picpay.banking.pix.original.dto.response.OriginalErrorDTO;
import org.springframework.http.HttpStatus;

public class NotFoundOriginalClientException extends OriginalClientException {

    public NotFoundOriginalClientException(HttpStatus status) {
        super(status);
    }

    public NotFoundOriginalClientException(OriginalErrorDTO error, HttpStatus status) {
        super(error, status);
    }

    public NotFoundOriginalClientException(String message, HttpStatus status) {
        super(message, status);
    }

    public NotFoundOriginalClientException(String message, OriginalErrorDTO error, HttpStatus status) {
        super(message, error, status);
    }

    public NotFoundOriginalClientException(String message, Throwable cause, OriginalErrorDTO error, HttpStatus status) {
        super(message, cause, error, status);
    }

}
