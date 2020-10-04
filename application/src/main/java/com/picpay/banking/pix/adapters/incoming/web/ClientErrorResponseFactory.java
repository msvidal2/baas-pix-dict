package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import com.picpay.banking.jdpi.exception.JDClientException;
import com.picpay.banking.pix.adapters.incoming.web.dto.ErrorDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.FieldErrorDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientErrorResponseFactory {

    public static ResponseEntity<ErrorDTO> newErrorDTO(final Throwable clientException) {
        if (clientException.getClass().isAssignableFrom(JDClientException.class)) {
            return getJDErrorResponse((JDClientException) clientException);
        }

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ErrorDTO.from(INTERNAL_SERVER_ERROR, clientException.getMessage()));
    }

    private static ResponseEntity<ErrorDTO> getJDErrorResponse(JDClientException clientException) {
        final var error = clientException.getError()
                .orElse(new JDErrorDTO("", "", Collections.emptyList()));

        final var fieldsErrors = Optional.ofNullable(error.getErrors())
                .map(errors -> errors.stream()
                        .map(FieldErrorDTO::from)
                        .collect(Collectors.toList()))
                .orElse(null);

        var message = error.getMessage();

//        if(message.isBlank()) {
//            message = clientException.getMessage();
//        }

        var errorDto = ErrorDTO.from(clientException.getStatus(),
                message,
                fieldsErrors != null && fieldsErrors.size() > 0 ? fieldsErrors : null);

        var errorCode = clientException.getCode();

        if(errorCode != null) {
            errorDto = ErrorDTO.from(clientException.getStatus(),
                            errorCode.getMessage(),
                            fieldsErrors != null && fieldsErrors.size() > 0 ? fieldsErrors : null,
                            errorCode.getCode());
        }

        return ResponseEntity
                .status(clientException.getStatus())
                .body(errorDto);
    }

}
