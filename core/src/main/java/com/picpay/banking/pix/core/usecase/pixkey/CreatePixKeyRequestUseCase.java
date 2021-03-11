package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreatePixKeyRequestUseCase {

    public PixKey execute(final String requestIdentifier, final PixKey pixKey, final Reason reason) {
        // TODO: implement
    }

}
