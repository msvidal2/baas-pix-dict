package com.picpay.banking.fallbacks;

import com.picpay.banking.fallbacks.dto.Violation;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.Optional;

public interface FieldResolver {

    Map<String, String> fieldsMap();

    default FieldError resolve(Violation violation) {
        var field = fieldsMap().get(violation.getProperty().toLowerCase());

        return new FieldError(violation.getValue(),
                Optional.ofNullable(field).orElse(violation.getProperty()),
                violation.getReason());
    }

}
