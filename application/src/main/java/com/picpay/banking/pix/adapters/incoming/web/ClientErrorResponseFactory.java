package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import com.picpay.banking.jdpi.exception.JDClientException;
import com.picpay.banking.pix.adapters.incoming.web.dto.ErrorDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.FieldErrorDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientErrorResponseFactory {

    public static ResponseEntity<ErrorDTO> newErrorDTO(JDClientException clientException) {
        final var error = clientException.getError()
                .orElse(new JDErrorDTO(null, null, Collections.emptyList()));

        final var fieldsErrors = Optional.ofNullable(error.getErrors())
                .map(errors -> errors.stream()
                        .map(FieldErrorDTO::from)
                        .collect(Collectors.toList()))
                .orElse(null);

        var jdErrorCode = clientException.getJDErrorCode();

        ErrorDTO errorDto = null;

        if(jdErrorCode != null) {
            errorDto = ErrorDTO.from(clientException.getStatus(),
                    jdErrorCode.getMessage(),
                    fieldsErrors != null && fieldsErrors.size() > 0 ? fieldsErrors : null,
                    jdErrorCode.getCode());
        } else {
            errorDto = ErrorDTO.from(clientException.getStatus(),
                    error.getMessage(),
                    fieldsErrors != null && fieldsErrors.size() > 0 ? fieldsErrors : null);
        }

        return ResponseEntity
                .status(clientException.getStatus())
                .body(errorDto);
    }

}
