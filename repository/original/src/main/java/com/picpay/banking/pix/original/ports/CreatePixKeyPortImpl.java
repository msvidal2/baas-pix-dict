package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.CreatePixKeyPort;
import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyCreateDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private final MaintenancePixKeyClient maintenancePixKeyClient;

    @Override
    public PixKey createPixKey(PixKey pixKey, CreateReason reason, String requestIdentifier) {
        final var createAccessKeyDTO = CreateAccessKeyDTO.fromPixKey(pixKey, reason);

        final ResponseWrapperDTO<AccessKeyCreateDTO> response;

        if(KeyType.RANDOM.equals(pixKey.getType())) {
            response = maintenancePixKeyClient.createEvpPixKey(requestIdentifier, createAccessKeyDTO);
        } else {
            response = maintenancePixKeyClient.createPixKey(requestIdentifier, createAccessKeyDTO);
        }

        return PixKey.builder()
                .key(response.getData().getKey())
                .createdAt(response.getData().getCreationDate())
                .startPossessionAt(response.getData().getKeyOwnershipDate())
                .build();
    }

}
