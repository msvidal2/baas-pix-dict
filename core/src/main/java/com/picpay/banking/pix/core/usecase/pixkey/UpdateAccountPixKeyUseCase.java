package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class UpdateAccountPixKeyUseCase {

    private UpdateAccountPixKeyPort updateAccountPixKeyPort;
    private DictItemValidator dictItemValidator;

    public PixKey execute(@NonNull final String requestIdentifier,
                          @NonNull final PixKey pixKey,
                          @NonNull final UpdateReason reason) {

        dictItemValidator.validate(pixKey);

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }

        if(KeyType.RANDOM.equals(pixKey.getType()) && UpdateReason.CLIENT_REQUEST.equals(reason)) {
            throw new UseCaseException("Random keys cannot be updated per client requests");
        }

        return updateAccountPixKeyPort.updateAccount(requestIdentifier, pixKey, reason);
    }

}
