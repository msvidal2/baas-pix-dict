package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.ErrorDTO;
import com.picpay.banking.pix.original.exception.NotFoundOriginalClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class OriginalExceptionHandler {

    @ExceptionHandler(NotFoundOriginalClientException.class )
    @ResponseStatus(NOT_FOUND)
    public ErrorDTO handleRuntimeException(final NotFoundOriginalClientException ex) {
        log.error("error", ex);

        return ErrorDTO.from(NOT_FOUND,
                Optional.ofNullable(ex.getMessage()).orElse("Resource not found"));
    }

}