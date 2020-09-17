package com.picpay.banking.jdpi.ports.pixkey;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.FindPixKeyConverter;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindPixKeyPortImpl implements FindPixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    private FindPixKeyConverter converter;

    @Override
    public PixKey findPixKey(String requestIdentifier, String key, String userId) {
        var findPixKeyResponseDTO =
                pixKeyJDClient.findPixKey(key, userId, null, null);

        return converter.convert(findPixKeyResponseDTO);
    }
}
