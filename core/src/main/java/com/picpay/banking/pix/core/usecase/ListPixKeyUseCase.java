package com.picpay.banking.pix.core.usecase;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.ListPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class ListPixKeyUseCase {

    private ListPixKeyPort listPixKeyPort;

    private DictItemValidator dictItemValidator;

    public Collection<PixKey> execute(
            final String requestIdentifier, final PixKey pixKey) {

        dictItemValidator.validate(pixKey);

        validateRequestFields(requestIdentifier);

        return listPixKeyPort.listPixKey(requestIdentifier, pixKey);
    }

    public void validateRequestFields(final String requestIdentifier) {
        if (Strings.isNullOrEmpty(requestIdentifier)) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }
    }
}
