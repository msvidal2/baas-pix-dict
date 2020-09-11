package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.CreatePixKeyPort;
import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private final MaintenancePixKeyClient maintenancePixKeyClient;

    @Override
    public PixKey createPixKey(PixKey pixKey, CreateReason reason, String requestIdentifier) {
        final var createAccessKeyDTO = CreateAccessKeyDTO.fromPixKey(pixKey, reason);

        final var response = maintenancePixKeyClient.create(requestIdentifier, createAccessKeyDTO);

        // TODO: implementar response

        return null;
    }

}
