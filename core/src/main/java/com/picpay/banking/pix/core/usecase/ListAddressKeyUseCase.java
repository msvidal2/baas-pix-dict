package com.picpay.banking.pix.core.usecase;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.ports.ListAddressingKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class ListAddressKeyUseCase {

    ListAddressingKeyPort listAddressingKeyPort;
    private DictItemValidator dictItemValidator;

    public Collection<AddressingKey> listAddressKeyUseCase(
            final String requestIdentifier, final AddressingKey addressingKey) {

        dictItemValidator.validate(addressingKey);

        validateRequestFields(requestIdentifier);

        return listAddressingKeyPort.listAddressingKey(requestIdentifier, addressingKey);
    }

    public void validateRequestFields(final String requestIdentifier) {
        if (Strings.isNullOrEmpty(requestIdentifier)) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }
    }
}
