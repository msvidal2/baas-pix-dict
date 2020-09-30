package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.RemovePixKeyRequestDTO;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RemovePixKeyPortImpl implements RemovePixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    @Trace
    @Override
    public PixKey remove(final String requestIdentifier, final PixKey pixKey, final RemoveReason reason) {
        final var requestDTO = RemovePixKeyRequestDTO.from(pixKey, reason);

        final var responseDTO = pixKeyJDClient.removeKey(requestIdentifier,
                pixKey.getKey(),
                requestDTO);

        return responseDTO.toDomain();
    }

}
