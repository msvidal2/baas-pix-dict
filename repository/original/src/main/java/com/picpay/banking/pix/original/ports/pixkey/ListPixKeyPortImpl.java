package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.ListPixKeyPort;
import com.picpay.banking.pix.original.clients.AccessKeyClient;
import com.picpay.banking.pix.original.dto.response.ListPixKeyResponseDTO;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class ListPixKeyPortImpl implements ListPixKeyPort {

    private AccessKeyClient accessKeyClient;

    @Override
    public List<PixKey> listPixKey(final String requestIdentifier, final PixKey pixKey) {

        var response = accessKeyClient.listPixKey(requestIdentifier, pixKey.getTaxId());

        List<ListPixKeyResponseDTO> retorno = response.getData();

        if(response.getData() == null) {
            return Collections.EMPTY_LIST;
        }

        return retorno.stream().map(ListPixKeyResponseDTO::toDomain).collect(Collectors.toList());
    }

}
