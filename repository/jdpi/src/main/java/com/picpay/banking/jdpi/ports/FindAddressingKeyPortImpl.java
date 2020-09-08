package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.converter.FindAddressingKeyConverter;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.ports.FindAddressingKeyPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindAddressingKeyPortImpl implements FindAddressingKeyPort {

    private AddressingKeyJDClient addressingKeyJDClient;

    private FindAddressingKeyConverter converter;

    @Override
    public AddressingKey findAddressingKey(final AddressingKey addressingKey, final String userId) {

        var findAddressingKeyResponseDTO =
                addressingKeyJDClient.findAddressingKey(addressingKey.getKey(),userId, null, null);

        return converter.convert(findAddressingKeyResponseDTO);
    }

}
