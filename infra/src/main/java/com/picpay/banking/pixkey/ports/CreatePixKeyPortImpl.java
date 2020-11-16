package com.picpay.banking.pixkey.ports;

import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import com.picpay.banking.pixkey.dto.request.CreateEntryRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private TimeLimiterExecutor timeLimiterExecutor;

    @Override
    public PixKey createPixKey(String requestIdentifier, PixKey pixKey, CreateReason reason) {

        CreateEntryRequest createEntryRequest = CreateEntryRequest.from(pixKey, reason, requestIdentifier);

        return null;
    }

}
