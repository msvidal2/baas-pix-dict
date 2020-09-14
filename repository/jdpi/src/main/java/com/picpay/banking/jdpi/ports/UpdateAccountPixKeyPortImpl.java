package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.UpdateAccountPixKeyRequestDTO;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.UpdateAccountPixKeyPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateAccountPixKeyPortImpl implements UpdateAccountPixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    @Override
    public PixKey updateAccount(PixKey pixKey, UpdateReason reason, String requestIdentifier) {
        final var requestDTO = UpdateAccountPixKeyRequestDTO.builder()
                .key(pixKey.getKey())
                .ispb(pixKey.getIspb())
                .branchNumber(pixKey.getBranchNumber())
                .accountType(pixKey.getAccountType().getValue())
                .accountNumber(pixKey.getAccountNumber())
                .accountOpeningDate(pixKey.getAccountOpeningDate())
                .reason(reason.getValue())
                .build();

        var responseDTO = pixKeyJDClient.updateAccount(requestIdentifier,
                pixKey.getKey(),
                requestDTO);

        return PixKey.builder()
                .key(responseDTO.getKey())
                .createdAt(responseDTO.getCreatedAt())
                .startPossessionAt(responseDTO.getStartPossessionAt())
                .build();
    }

}
