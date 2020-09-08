package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.UpdateAccountAddressingKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class UpdateAccountAddressingKeyUseCase {

    private UpdateAccountAddressingKeyPort updateAccountAddressingKeyPort;
    private DictItemValidator dictItemValidator;

    public AddressingKey update(@NonNull final AddressingKey addressingKey,
                                @NonNull final UpdateReason reason,
                                @NonNull final String requestIdentifier) {

        dictItemValidator.validate(addressingKey);

        validateRequestFields(requestIdentifier, reason, addressingKey.getType());

        return updateAccountAddressingKeyPort.updateAccount(addressingKey, reason, requestIdentifier);
    }

    private void validateRequestFields(
            final String requestIdentifier, final UpdateReason reason, final KeyType keyType) {

        if(KeyType.EVP.equals(keyType) && UpdateReason.CLIENT_REQUEST.equals(reason)) {
            throw new UseCaseException("EVP keys cannot be updated per client requests");
        }

        if (reason == null) {
            throw new IllegalArgumentException("reason can not be empty");
        }

        if(requestIdentifier.isBlank()) {
            throw new UseCaseException("Invalid request identifier");
        }
    }
}
