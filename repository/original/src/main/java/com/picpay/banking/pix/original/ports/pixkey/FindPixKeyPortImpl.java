package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;
import com.picpay.banking.pix.original.clients.AccessKeyClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FindPixKeyPortImpl implements FindPixKeyPort {

    private AccessKeyClient accessKeyClient;

    @Override
    public PixKey findPixKey(String requestIdentifier, String pixKey, String userId) {
        var responseDTO = accessKeyClient.findByKey(requestIdentifier, pixKey, userId);

        return responseDTO.getData().toDomain();
    }

}