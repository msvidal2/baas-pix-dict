package com.picpay.banking.pix.adapters.incoming.web;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.picpay.banking.jdpi.exception.JDClientException;
import com.picpay.banking.pix.adapters.incoming.web.dto.ErrorDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.FieldErrorDTO;
import com.picpay.banking.pix.core.validators.key.KeyValidator;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class CustomExceptionHandler {

//    @ExceptionHandler({HystrixRuntimeException.class})
//    public ResponseEntity<ErrorDTO> handleClientException(final HystrixRuntimeException hystrixRuntimeException) {
//        log.error(hystrixRuntimeException.getMessage(), hystrixRuntimeException);
//
//        return ClientErrorResponseFactory.newErrorDTO(hystrixRuntimeException
//                .getFallbackException()
//                .getCause()
//                .getCause());
//    }

    @ExceptionHandler({JDClientException.class})
    public ResponseEntity<ErrorDTO> jdClientException(final JDClientException ex) {
        log.error(ex.getMessage(), ex);

        return ClientErrorResponseFactory.newErrorDTO(ex);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            KeyValidatorException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleRuntimeException(final RuntimeException ex, final WebRequest request) {
        log.error("error", ex);

        return ErrorDTO.from(BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorDTO handleInternalException(final Exception ex, final WebRequest request) {
        log.error("error", ex);

        return ErrorDTO.from(INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldErrorDTO::from)
                .collect(toList());

        return ErrorDTO.from(BAD_REQUEST, "Invalid Arguments", fieldErrors);
    }

    @ExceptionHandler({BindException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleMethodArgumentNotValid(final BindException ex) {
        List<FieldErrorDTO> fieldErrors = ex.getFieldErrors()
            .stream()
            .map(FieldErrorDTO::from).collect(toList());

        return ErrorDTO.from(BAD_REQUEST, "Invalid Arguments", fieldErrors);
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleMissingRequestHeaderException(final MissingRequestHeaderException ex) {
        log.error("error", ex);

        return ErrorDTO.from(BAD_REQUEST, ex.getMessage());
    }

}