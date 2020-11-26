package com.picpay.banking.pix.core.validators;

import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestIdentifierValidator {

    public static void validate(String requestIdentifier) {
        if (Strings.isNullOrEmpty(requestIdentifier) || requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier cannot be empty");
        }
    }

}
