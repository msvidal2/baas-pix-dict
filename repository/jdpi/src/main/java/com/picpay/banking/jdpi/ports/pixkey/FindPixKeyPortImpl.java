package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.FindPixKeyConverter;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindPixKeyPortImpl implements FindPixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    private FindPixKeyConverter converter;

    @Trace
    @Override
    public PixKey findPixKey(String requestIdentifier, String pixKey, String userId) {
        var findPixKeyResponseDTO =
                pixKeyJDClient.findPixKey(pixKey, userId, null, null);

        return converter.convert(findPixKeyResponseDTO);
    }
}
