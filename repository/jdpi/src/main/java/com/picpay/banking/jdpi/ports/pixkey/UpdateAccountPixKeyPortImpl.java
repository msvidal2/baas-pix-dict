package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.UpdateAccountPixKeyRequestDTO;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateAccountPixKeyPortImpl implements UpdateAccountPixKeyPort {

    private PixKeyJDClient pixKeyJDClient;

    @Trace
    @Override
    public PixKey updateAccount(String requestIdentifier, PixKey pixKey, UpdateReason reason) {
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
