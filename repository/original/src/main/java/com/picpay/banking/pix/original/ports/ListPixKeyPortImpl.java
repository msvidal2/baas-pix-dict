package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.ListPixKeyPort;
import com.picpay.banking.pix.original.clients.SearchPixKeyClient;
import com.picpay.banking.pix.original.dto.request.ListPixKeyRequestDTO;
import lombok.AllArgsConstructor;

import java.util.Collection;


@AllArgsConstructor
public class ListPixKeyPortImpl implements ListPixKeyPort {

    private SearchPixKeyClient searchPixKeyClient;

    @Override
    public Collection<PixKey> listPixKey(final String requestIdentifier, final PixKey pixKey) {

        var listPixKeyRequestDTO = ListPixKeyRequestDTO.from(pixKey);

        return searchPixKeyClient.listPixKey(listPixKeyRequestDTO).getData().toDomain();

    }
}
