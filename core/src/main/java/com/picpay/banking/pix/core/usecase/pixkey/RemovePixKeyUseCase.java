package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class RemovePixKeyUseCase {

    private RemovePixKeyPort removePixKeyPort;
    private DictItemValidator dictItemValidator;

    public void execute(@NonNull final String requestIdentifier,
                        @NonNull final PixKey pixKey,
                        @NonNull final RemoveReason reason) {
        dictItemValidator.validate(pixKey);

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }

        removePixKeyPort.remove(requestIdentifier, pixKey, reason);
    }

}
