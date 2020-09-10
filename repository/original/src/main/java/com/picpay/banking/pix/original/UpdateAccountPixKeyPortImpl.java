package com.picpay.banking.pix.original;

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
        final var requestDTO = UpdateAccessKeyAccountDTO.builder()
                .key(pixKey.getKey())
                .accountType(pixKey.getAccountType().name()) //TODO: fazer de-para
                .accountNumber(pixKey.getAccountNumber())
                .accountOpeningDate(pixKey.getAccountOpeningDate())
                .reason(reason.name()) //TODO: fazer de-para
                .build();

        final var responseDTO = maintenancePixKeyClient.update(requestDTO);

        return PixKey.builder()
                .createdAt(responseDTO.getData().getCreationDate())
                .startPossessionAt(responseDTO.getData().getKeyOwnershipDate())
                .build();
    }

}
