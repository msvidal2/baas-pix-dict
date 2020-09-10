package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class UpdateAccountPixKeyUseCase {

    private UpdateAccountPixKeyPort updateAccountPixKeyPort;
    private DictItemValidator dictItemValidator;

    public PixKey update(@NonNull final PixKey pixKey,
                         @NonNull final UpdateReason reason,
                         @NonNull final String requestIdentifier) {

        dictItemValidator.validate(pixKey);

        validateRequestFields(requestIdentifier, reason, pixKey.getType());

        return updateAccountPixKeyPort.updateAccount(pixKey, reason, requestIdentifier);
    }

    private void validateRequestFields(
            final String requestIdentifier, final UpdateReason reason, final KeyType keyType) {

        if(KeyType.RANDOM.equals(keyType) && UpdateReason.CLIENT_REQUEST.equals(reason)) {
            throw new UseCaseException("Random keys cannot be updated per client requests");
        }

        if (reason == null) {
            throw new IllegalArgumentException("reason can not be empty");
        }

        if(requestIdentifier.isBlank()) {
            throw new UseCaseException("Invalid request identifier");
        }
    }
}
