package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.request.UpdateAccessKeyAccountDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateAccountPixKeyPortImpl implements UpdateAccountPixKeyPort {

    private MaintenancePixKeyClient maintenancePixKeyClient;

    @Override
    public PixKey updateAccount(PixKey pixKey, UpdateReason reason, String requestIdentifier) {
        final var requestDTO = UpdateAccessKeyAccountDTO.from(pixKey, reason);

        final var responseDTO = maintenancePixKeyClient.update(requestDTO);

        return responseDTO.getData().toDomain();
    }

}
