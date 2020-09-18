package com.picpay.banking.pix.adapters.incoming.web;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import com.picpay.banking.jdpi.exception.JDClientException;
import com.picpay.banking.pix.adapters.incoming.web.dto.ErrorDTO;
import com.picpay.banking.pix.original.dto.response.OriginalErrorDTO;
import com.picpay.banking.pix.original.exception.OriginalClientException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientErrorResponseFactory {

    public static ResponseEntity<ErrorDTO> newErrorDTO(final Throwable clientException) {
        if (clientException.getClass().isAssignableFrom(JDClientException.class)) {
            return getJDErrorResponse((JDClientException) clientException);

        } else if (clientException.getClass().isAssignableFrom(OriginalClientException.class)) {
            return getOriginalErrorResponse((OriginalClientException) clientException);
        }

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ErrorDTO.from(INTERNAL_SERVER_ERROR, clientException.getMessage()));
    }

    private static ResponseEntity<ErrorDTO> getOriginalErrorResponse(OriginalClientException clientException) {
        var message = clientException.getError()
                .orElse(new OriginalErrorDTO("", "", Collections.emptyList()))
                .getError();

        return ResponseEntity
                .status(clientException.getStatus())
                .body(ErrorDTO.from(clientException.getStatus(), message));
    }

    private static ResponseEntity<ErrorDTO> getJDErrorResponse(JDClientException clientException) {
        var message = clientException.getError()
                .orElse(new JDErrorDTO("", "", Collections.emptyList()))
                .getMessage();

        return ResponseEntity
                .status(clientException.getStatus())
                .body(ErrorDTO.from(clientException.getStatus(), message));
    }

}
