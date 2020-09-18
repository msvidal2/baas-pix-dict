package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class FindPixKeyUseCase {

    private FindPixKeyPort findPixKeyPort;

    public PixKey execute(@NonNull final String requestIdentifier,
                          @NonNull final String pixKey,
                          @NonNull final String userId)  {

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }

        if(userId.isBlank()) {
            throw new IllegalArgumentException("The userId can not be null");
        }

        return findPixKeyPort.findPixKey(requestIdentifier, pixKey, userId);
    }

}
