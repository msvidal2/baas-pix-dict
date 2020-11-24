package com.picpay.banking.fallbacks;

import lombok.*;
import org.springframework.validation.FieldError;

import java.util.List;

@Builder
@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor
public class BacenError {

    private String message;
    private String detail;
    private List<FieldError> fields;
    private String correlationId;
}
