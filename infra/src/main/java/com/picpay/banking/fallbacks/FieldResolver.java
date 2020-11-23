package com.picpay.banking.fallbacks;

import com.picpay.banking.fallbacks.dto.Violation;
import org.springframework.validation.FieldError;

public interface FieldResolver {

    FieldError resolve(Violation violation);

}
