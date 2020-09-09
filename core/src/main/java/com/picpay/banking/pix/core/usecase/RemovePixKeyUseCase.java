package com.picpay.banking.pix.core.usecase;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RemovePixKeyUseCase {

    private RemovePixKeyPort removePixKeyPort;
    private DictItemValidator dictItemValidator;

    public void remove(final PixKey pixKey, final RemoveReason reason, final String requestIdentifier) {
        dictItemValidator.validate(pixKey);

        validateRequestFields(reason, requestIdentifier);

        removePixKeyPort.remove(pixKey, reason, requestIdentifier);
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
