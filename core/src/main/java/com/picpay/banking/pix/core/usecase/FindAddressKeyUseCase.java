package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.ports.FindAddressingKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class FindAddressKeyUseCase {

    private FindAddressingKeyPort findAddressingKeyPort;
    private DictItemValidator dictItemValidator;

    public AddressingKey findAddressKeyUseCase(@NonNull final AddressingKey addressingKey,
                                               @NonNull final String userId)  {

        if(userId.isBlank()) {
            throw new IllegalArgumentException("The userId can not be null");
        }

        return findAddressingKeyPort.findAddressingKey(addressingKey, userId);
    }

}
