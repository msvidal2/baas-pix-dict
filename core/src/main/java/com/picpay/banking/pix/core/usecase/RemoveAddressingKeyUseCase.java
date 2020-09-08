package com.picpay.banking.pix.core.usecase;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.RemoveAddressingKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RemoveAddressingKeyUseCase {

    private RemoveAddressingKeyPort removeAddressingKeyPort;
    private DictItemValidator dictItemValidator;

    public void remove(final AddressingKey addressingKey, final RemoveReason reason, final String requestIdentifier) {
        dictItemValidator.validate(addressingKey);

        validateRequestFields(reason, requestIdentifier);

        removeAddressingKeyPort.remove(addressingKey, reason, requestIdentifier);
    }

    public void validateRequestFields(final RemoveReason reason, final String requestIdentifier) {
        if (Strings.isNullOrEmpty(requestIdentifier)) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }
        if (reason == null) {
            throw new IllegalArgumentException("reason can not be empty");
        }
    }
}
