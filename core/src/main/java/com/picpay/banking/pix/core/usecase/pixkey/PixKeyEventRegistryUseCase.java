package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeyEvent;
import com.picpay.banking.pix.core.domain.Reason;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PixKeyEventRegistryUseCase {

    public void execute(final PixKeyEvent event,
                        final String requestIdentifier,
                        final PixKey key,
                        final Reason reason) {



    }

}
