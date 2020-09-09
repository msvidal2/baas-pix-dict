package com.picpay.banking.pix.core.usecase;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.ports.CreatePixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreatePixKeyUseCase {

    private CreatePixKeyPort createPixKeyPort;
    private DictItemValidator dictItemValidator;

    public PixKey createAddressKeyUseCase(
            final PixKey pixKey, final CreateReason reason, final String requestIdentifier) {

        dictItemValidator.validate(pixKey);

        validateRequestFields(reason, requestIdentifier);

        return createPixKeyPort.createPixKey(pixKey, reason, requestIdentifier);
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
