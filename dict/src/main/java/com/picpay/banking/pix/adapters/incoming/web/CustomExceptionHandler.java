package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.exceptions.BacenException;
import com.picpay.banking.jdpi.exception.JDClientException;
import com.picpay.banking.jdpi.exception.NotFoundJdClientException;
import com.picpay.banking.pix.adapters.incoming.web.dto.ErrorDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.FieldErrorDTO;
import com.picpay.banking.pix.core.exception.PixKeyException;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BacenException.class)
    public ResponseEntity<ErrorDTO> handleBacenException(BacenException e) {
        var bacenError = e.getBacenError();

        var errorBuilder = ErrorDTO.builder()
                .code(e.getHttpStatus().value())
                .error(e.getHttpStatus().getReasonPhrase())
                .apiErrorCode(bacenError.getMessage())
                .message(bacenError.getDetail());

        if(bacenError.getFields() != null && bacenError.getFields().isEmpty()) {
            var fields = bacenError.getFields()
                    .stream()
                    .map(FieldErrorDTO::from)
                    .collect(Collectors.toList());

            errorBuilder.fieldErrors(fields);
        }

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(errorBuilder.build());
    }

    @ExceptionHandler(PixKeyException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handlePixKeyException(PixKeyException e) {
        var errorBuilder = ErrorDTO.builder()
                .code(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now());

        if(!Objects.isNull(e.getPixKeyError())) {
            errorBuilder.apiErrorCode(e.getPixKeyError().getCode())
                    .message(e.getPixKeyError().getMessage());
        }

        var error = errorBuilder.build();

        log.error("error_handlePixKeyException", error.toLogJson(e));

        return error;
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            KeyValidatorException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(final RuntimeException ex, final WebRequest request) {
        ErrorDTO errorDTO = ErrorDTO.from(BAD_REQUEST, ex.getMessage());

        log.error("error_handleIllegalArgumentException", errorDTO.toLogJson(ex));

        return errorDTO;
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorDTO handleInternalException(final Exception ex, final WebRequest request) {
        ErrorDTO errorDTO = ErrorDTO.from(INTERNAL_SERVER_ERROR, ex.getMessage());

        log.error("error_handleInternalException", errorDTO.toLogJson(ex));

        log.error(ex.getMessage(), ex);

        return errorDTO;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldErrorDTO::from)
                .collect(toList());

        ErrorDTO errorDTO = ErrorDTO.from(BAD_REQUEST, "Invalid Arguments", fieldErrors);

        log.error("error_handleMethodArgumentNotValidException", errorDTO.toLogJson(ex));

        return errorDTO;
    }

    @ExceptionHandler({BindException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleBindException(final BindException ex) {
        List<FieldErrorDTO> fieldErrors = ex.getFieldErrors()
            .stream()
            .map(FieldErrorDTO::from).collect(toList());

        ErrorDTO errorDTO = ErrorDTO.from(BAD_REQUEST, "Invalid Arguments", fieldErrors);

        log.error("error_handleBindException", errorDTO.toLogJson(ex));

        return errorDTO;
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleMissingRequestHeaderException(final MissingRequestHeaderException ex) {
        ErrorDTO errorDTO = ErrorDTO.from(BAD_REQUEST, ex.getMessage());

        log.error("error_handleMissingRequestHeaderException", errorDTO.toLogJson(ex));

        return errorDTO;
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ErrorDTO handleNotFoundException(Exception ex, WebRequest webRequest){
        ErrorDTO errorDTO = ErrorDTO.from(NOT_FOUND, ex.getMessage());

        log.error("error_resourceNotFoundException", errorDTO.toLogJson(ex));

        return errorDTO;
    }

}