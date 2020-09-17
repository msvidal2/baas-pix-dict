package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;
import com.picpay.banking.pix.original.clients.AccessKeyClient;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindPixKeyPortImpl  implements FindPixKeyPort {

    private AccessKeyClient accessKeyClient;

    @Override
    public PixKey findPixKey(String requestIdentifier, String key, String userId) {
        var responseDTO = accessKeyClient.findByKey(requestIdentifier, key, userId);

        return responseDTO.getData().toDomain();
    }

}