package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.CreatePixKeyConverter;
import com.picpay.banking.jdpi.dto.request.CreatePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreatePixKeyResponseJDDTO;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.CreatePixKeyPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    private CreatePixKeyConverter converter;

    public CreatePixKeyPortImpl(
            PixKeyJDClient pixKeyJDClient, CreatePixKeyConverter converter) {
        this.pixKeyJDClient = pixKeyJDClient;
        this.converter = converter;
    }

    @Override
    public PixKey createPixKey(PixKey pixKey, CreateReason reason, String requestIdentifier) {
        CreatePixKeyRequestDTO requestDTO = converter.convert(pixKey, reason);

        CreatePixKeyResponseJDDTO jdpiReturnDTO = pixKeyJDClient.createPixKey(requestIdentifier, requestDTO);

        return converter.convert(jdpiReturnDTO, pixKey);
    }
}
