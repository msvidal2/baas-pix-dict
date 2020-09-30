package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.CreatePixKeyConverter;
import com.picpay.banking.jdpi.dto.request.CreatePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreatePixKeyResponseJDDTO;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    private CreatePixKeyConverter converter;

    @Trace
    @Override
    public PixKey createPixKey(String requestIdentifier, PixKey pixKey, CreateReason reason) {
        CreatePixKeyRequestDTO requestDTO = converter.convert(pixKey, reason);

        CreatePixKeyResponseJDDTO jdpiReturnDTO = pixKeyJDClient.createPixKey(requestIdentifier, requestDTO);

        return converter.convert(jdpiReturnDTO, pixKey);
    }
}
