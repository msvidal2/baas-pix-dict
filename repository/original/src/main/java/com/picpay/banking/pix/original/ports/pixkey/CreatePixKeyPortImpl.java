package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import com.picpay.banking.pix.original.clients.AccessKeyClient;
import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyCreateDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private final AccessKeyClient accessKeyClient;

    @Override
    public PixKey createPixKey(String requestIdentifier, PixKey pixKey, CreateReason reason) {
        final var createAccessKeyDTO = CreateAccessKeyDTO.fromPixKey(pixKey, reason);

        final ResponseWrapperDTO<AccessKeyCreateDTO> response;

        if(KeyType.RANDOM.equals(pixKey.getType())) {
            response = accessKeyClient.createEvpPixKey(requestIdentifier, createAccessKeyDTO);
        } else {
            response = accessKeyClient.createPixKey(requestIdentifier, createAccessKeyDTO);
        }

        return PixKey.builder()
                .key(response.getData().getKey())
                .createdAt(response.getData().getCreationDate())
                .startPossessionAt(response.getData().getKeyOwnershipDate())
                .build();
    }

}
