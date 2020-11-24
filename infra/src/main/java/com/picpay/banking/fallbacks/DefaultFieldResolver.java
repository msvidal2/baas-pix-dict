package com.picpay.banking.fallbacks;

import com.picpay.banking.fallbacks.dto.Violation;
import org.springframework.validation.FieldError;

public class DefaultFieldResolver implements FieldResolver {

    public FieldError resolve(Violation violation) {
        return new FieldError(violation.getProperty(), violation.getProperty(), violation.getReason());
    }

}
