package com.picpay.banking.fallbacks;

import lombok.*;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor
public class BacenError implements Serializable {

    private static final long serialVersionUID = -3659200961446381933L;

    private String message;
    private String detail;
    private List<FieldError> fields;
    private String correlationId;
    private boolean isRetryable;

}
