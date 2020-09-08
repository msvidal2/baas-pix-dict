package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.dto.request.RemoveAddressingKeyRequestDTO;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.RemoveAddressingKeyPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoveAddressingKeyPortImpl implements RemoveAddressingKeyPort {

    private AddressingKeyJDClient addressingKeyJDClient;

    public RemoveAddressingKeyPortImpl(AddressingKeyJDClient addressingKeyJDClient) {
        this.addressingKeyJDClient = addressingKeyJDClient;
    }

    @Override
    public void remove(final AddressingKey addressingKey, final RemoveReason reason, final String requestIdentifier) {
        final var requestDTO = RemoveAddressingKeyRequestDTO.builder()
                .key(addressingKey.getKey())
                .reason(reason.getValue())
                .ispb(addressingKey.getIspb())
                .build();

        addressingKeyJDClient.removeKey(requestIdentifier,
                addressingKey.getKey(),
                requestDTO);
    }

}
