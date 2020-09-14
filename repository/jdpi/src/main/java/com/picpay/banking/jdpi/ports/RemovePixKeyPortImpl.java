package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.RemovePixKeyRequestDTO;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.RemovePixKeyPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RemovePixKeyPortImpl implements RemovePixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    @Override
    public PixKey remove(final PixKey pixKey, final RemoveReason reason, final String requestIdentifier) {
        final var requestDTO = RemovePixKeyRequestDTO.from(pixKey, reason);

        final var responseDTO = pixKeyJDClient.removeKey(requestIdentifier,
                pixKey.getKey(),
                requestDTO);

        return responseDTO.toDomain();
    }

}
