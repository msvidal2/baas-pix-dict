package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.ListPixKeyPort;
import com.picpay.banking.pix.original.clients.SearchPixKeyClient;
import com.picpay.banking.pix.original.converter.ListPixKeyConverter;
import com.picpay.banking.pix.original.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.pix.original.dto.request.PixKeyRequestDTO;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collection;


@AllArgsConstructor
public class ListPixKeyPortImpl implements ListPixKeyPort {

    private SearchPixKeyClient searchPixKeyClient;

    private ListPixKeyConverter converter;

    @Override
    public Collection<PixKey> listPixKey(final String requestIdentifier, final PixKey pixKey) {

        var pixKeyRequestDTO = PixKeyRequestDTO.builder()
            .key(pixKey.getKey())
            .responsibleKey(String.valueOf(pixKey.getCpfCnpj()))
            .build();

        var listPixKeyRequestDTO = ListPixKeyRequestDTO.builder()
            .listKey(Arrays.asList(pixKeyRequestDTO))
            .build();

        var listPixKeyResponseDTO = searchPixKeyClient.listPixKey(listPixKeyRequestDTO);

        return converter.convert(listPixKeyResponseDTO);
    }

}
