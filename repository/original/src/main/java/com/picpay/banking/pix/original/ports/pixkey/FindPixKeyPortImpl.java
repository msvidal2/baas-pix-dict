package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;
import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FindPixKeyPortImpl implements FindPixKeyPort {

    private MaintenancePixKeyClient maintenancePixKeyClient;

    @Override
    public PixKey findPixKey(String requestIdentifier, PixKey pixKey, String userId) {
        var responseDTO = maintenancePixKeyClient.findByKey(requestIdentifier, pixKey.getKey(), userId);

        return responseDTO.getData().toDomain();
    }

}