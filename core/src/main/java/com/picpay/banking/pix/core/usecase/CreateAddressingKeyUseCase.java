package com.picpay.banking.pix.core.usecase;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.ports.CreateAddressingKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateAddressingKeyUseCase {

    private CreateAddressingKeyPort createAddressingKeyPort;
    private DictItemValidator dictItemValidator;

    public AddressingKey createAddressKeyUseCase(
            final AddressingKey addressingKey, final CreateReason reason, final String requestIdentifier) {

        dictItemValidator.validate(addressingKey);

        validateRequestFields(reason, requestIdentifier);

        return createAddressingKeyPort.createAddressingKey(addressingKey, reason, requestIdentifier);
    }

    public void validateRequestFields(final CreateReason reason, final String requestIdentifier) {
        if (Strings.isNullOrEmpty(requestIdentifier)) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }
        if (reason == null) {
            throw new IllegalArgumentException("reason can not be empty");
        }
    }

}
