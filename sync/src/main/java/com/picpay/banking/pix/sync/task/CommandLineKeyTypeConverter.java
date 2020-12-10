package com.picpay.banking.pix.sync.task;

import com.beust.jcommander.IStringConverter;
import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.KeyType;

public class CommandLineKeyTypeConverter implements IStringConverter<KeyType> {

    @Override
    public KeyType convert(final String value) {
        final String errorMessage = "The type of the key(-keyType argument) must contain one of the following values: CPF, CNPJ, EMAIL, CELLPHONE, RANDOM.";

        if (Strings.isNullOrEmpty(value) || value.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }

        try {
            return KeyType.valueOf(value.toUpperCase());
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
