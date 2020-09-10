package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.RemovePixKeyRequestDTO;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.RemovePixKeyPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemovePixKeyPortImpl implements RemovePixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    public RemovePixKeyPortImpl(PixKeyJDClient pixKeyJDClient) {
        this.pixKeyJDClient = pixKeyJDClient;
    }

    @Override
    public void remove(final PixKey pixKey, final RemoveReason reason, final String requestIdentifier) {
        final var requestDTO = RemovePixKeyRequestDTO.builder()
                .key(pixKey.getKey())
                .reason(reason.getValue())
                .ispb(pixKey.getIspb())
                .build();

        pixKeyJDClient.removeKey(requestIdentifier,
                pixKey.getKey(),
                requestDTO);
    }

}
