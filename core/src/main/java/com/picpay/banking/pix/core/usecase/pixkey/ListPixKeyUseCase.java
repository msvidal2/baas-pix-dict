package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.ListPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Collection;

@AllArgsConstructor
public class ListPixKeyUseCase {

    private ListPixKeyPort listPixKeyPort;

    private DictItemValidator dictItemValidator;

    public Collection<PixKey> execute(@NonNull final String requestIdentifier,
                                      @NonNull final PixKey pixKey) {
        dictItemValidator.validate(pixKey);

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }

        return listPixKeyPort.listPixKey(requestIdentifier, pixKey);
    }

}