package com.picpay.banking.pix.processor;

import com.picpay.banking.exceptions.BacenException;
import com.picpay.banking.pix.core.events.data.ErrorEventData;
import com.picpay.banking.pix.core.events.data.FieldData;
import com.picpay.banking.pix.core.exception.UseCaseException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorEventDataFactory {

    public static ErrorEventData fromBacenException(BacenException e) {
        var bacenError = e.getBacenError();

        var fields = Optional.ofNullable(bacenError.getFields())
                .orElse(Collections.emptyList())
                .stream()
                .map(f -> FieldData.builder()
                        .message(f.getDefaultMessage())
                        .value(String.valueOf(f.getRejectedValue()))
                        .property(f.getField())
                        .build())
                .collect(Collectors.toList());

        return ErrorEventData.builder()
                .code(bacenError.getMessage())
                .description(bacenError.getDetail())
                .fields(fields)
                .build();
    }

    public static ErrorEventData fromUseCaseException(UseCaseException e) {
        return ErrorEventData.builder()
                .code(e.getCode())
                .description(e.getMessage())
                .build();
    }

    public static ErrorEventData fromException(Exception e) {
        return ErrorEventData.builder()
                .code("InteralServerError")
                .description(e.getMessage())
                .build();
    }
}
