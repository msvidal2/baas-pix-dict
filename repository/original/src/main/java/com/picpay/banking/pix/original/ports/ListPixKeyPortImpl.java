package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.ListPixKeyPort;
import com.picpay.banking.pix.original.clients.SearchPixKeyClient;
import com.picpay.banking.pix.original.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.pix.original.dto.response.ListPixKeyDTO;
import com.picpay.banking.pix.original.dto.response.ListPixKeyResponseDTO;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class ListPixKeyPortImpl implements ListPixKeyPort {

    private SearchPixKeyClient searchPixKeyClient;

    @Override
    public List<PixKey> listPixKey(final String requestIdentifier, final PixKey pixKey) {
        var listPixKeyRequestDTO = ListPixKeyRequestDTO.from(pixKey);

        var response = searchPixKeyClient.listPixKey(requestIdentifier, listPixKeyRequestDTO);

        List<ListPixKeyDTO> retorno = response.getData();

        return retorno.stream().map(ListPixKeyDTO::getResponse).map(ListPixKeyResponseDTO::toDomain).collect(Collectors.toList());
    }

}
