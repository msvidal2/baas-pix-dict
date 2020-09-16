package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.request.RemoveAccessKeyDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RemovePixKeyPortImpl implements RemovePixKeyPort {

    private MaintenancePixKeyClient maintenancePixKeyClient;

    @Override
    public PixKey remove(String requestIdentifier, PixKey pixKey, RemoveReason reason) {
        final var requestDTO = RemoveAccessKeyDTO.from(pixKey, reason);

        final var responseDTO = maintenancePixKeyClient.remove(requestIdentifier, requestDTO);

        return responseDTO.getData().toDomain();
    }
}
