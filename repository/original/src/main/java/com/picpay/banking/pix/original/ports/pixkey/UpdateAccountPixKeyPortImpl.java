package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.original.clients.AccessKeyClient;
import com.picpay.banking.pix.original.dto.request.UpdateAccessKeyAccountDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateAccountPixKeyPortImpl implements UpdateAccountPixKeyPort {

    private AccessKeyClient accessKeyClient;

    @Override
    public PixKey updateAccount(String requestIdentifier, PixKey pixKey, UpdateReason reason) {
        final var requestDTO = UpdateAccessKeyAccountDTO.from(pixKey, reason);

        final var responseDTO = accessKeyClient.update(requestIdentifier, requestDTO);

        return responseDTO.getData().toDomain();
    }

}