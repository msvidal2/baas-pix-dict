package com.picpay.banking.fallbacks;

import com.picpay.banking.fallbacks.dto.Violation;
import org.springframework.validation.FieldError;

public class PixKeyFieldResolver implements FieldResolver {

    @Override
    public FieldError resolve(Violation violation) {
        return null;
    }

}
