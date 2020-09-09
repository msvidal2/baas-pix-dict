package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.converter.CreateAddressingKeyConverter;
import com.picpay.banking.jdpi.dto.request.CreateAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreateAddressingKeyResponseJDDTO;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.ports.CreateAddressingKeyPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateAddressingKeyPortImpl implements CreateAddressingKeyPort {

    private AddressingKeyJDClient addressingKeyJDClient;

    private CreateAddressingKeyConverter converter;

    public CreateAddressingKeyPortImpl(
            AddressingKeyJDClient addressingKeyJDClient, CreateAddressingKeyConverter converter) {
        this.addressingKeyJDClient = addressingKeyJDClient;
        this.converter = converter;
    }

    @Override
    public AddressingKey createAddressingKey(
            final AddressingKey addressingKey, final CreateReason reason, final String requestIdentifier) {
        CreateAddressingKeyRequestDTO requestDTO = converter.convert(addressingKey, reason);

        CreateAddressingKeyResponseJDDTO jdpiReturnDTO = addressingKeyJDClient.createAddressingKey(requestIdentifier, requestDTO);

        return converter.convert(jdpiReturnDTO, addressingKey);
    }

}
