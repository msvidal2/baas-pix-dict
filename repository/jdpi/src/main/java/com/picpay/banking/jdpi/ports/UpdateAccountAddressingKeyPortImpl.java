package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.dto.request.UpdateAccountAddressingKeyRequestDTO;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.UpdateAccountAddressingKeyPort;

public class UpdateAccountAddressingKeyPortImpl implements UpdateAccountAddressingKeyPort {

    private AddressingKeyJDClient addressingKeyJDClient;

    public UpdateAccountAddressingKeyPortImpl(AddressingKeyJDClient addressingKeyJDClient) {
        this.addressingKeyJDClient = addressingKeyJDClient;
    }

    @Override
    public AddressingKey updateAccount(AddressingKey addressingKey, UpdateReason reason, String requestIdentifier) {
        final var requestDTO = UpdateAccountAddressingKeyRequestDTO.builder()
                .key(addressingKey.getKey())
                .ispb(addressingKey.getIspb())
                .branchNumber(addressingKey.getBranchNumber())
                .accountType(addressingKey.getAccountType().getValue())
                .accountNumber(addressingKey.getAccountNumber())
                .accountOpeningDate(addressingKey.getAccountOpeningDate())
                .reason(reason.getValue())
                .build();

        var responseDTO = addressingKeyJDClient.updateAccount(requestIdentifier,
                addressingKey.getKey(),
                requestDTO);

        return AddressingKey.builder()
                .key(responseDTO.getKey())
                .createdAt(responseDTO.getCreatedAt())
                .startPossessionAt(responseDTO.getStartPossessionAt())
                .build();
    }

}
