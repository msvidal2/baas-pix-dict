package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.FindPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class FindPixKeyUseCase {

    private FindPixKeyPort findPixKeyPort;
    private DictItemValidator dictItemValidator;

    public PixKey execute(@NonNull final PixKey pixKey,
                          @NonNull final String userId)  {

        if(userId.isBlank()) {
            throw new IllegalArgumentException("The userId can not be null");
        }

        return findPixKeyPort.findPixKey(pixKey, userId);
    }

}
