package com.picpay.banking.jdpi.converter;

import com.picpay.banking.jdpi.dto.response.ListAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListKeyResponseDTO;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.ClaimType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ListAddressingKeyConverter implements DataConverter<ListAddressingKeyResponseDTO, Collection<AddressingKey>> {

    @Override
    public Collection<AddressingKey> convert(final ListAddressingKeyResponseDTO from) {
        return from.getChavesAssociadas().stream().map(this::getAddressingKey).collect(Collectors.toList());
    }

    private AddressingKey getAddressingKey(final ListKeyResponseDTO listKeyResponseDTO) {
        return AddressingKey.builder()
            .key(listKeyResponseDTO.getChave())
            .name(listKeyResponseDTO.getNome())
            .fantasyName(listKeyResponseDTO.getNomeFantasia())
            .createdAt(listKeyResponseDTO.getDtHrCriacaoChave())
            .startPossessionAt(listKeyResponseDTO.getDtHrInicioPosseChave())
            .claim(ClaimType.resolve(listKeyResponseDTO.getReivindicacao())).build();
    }
}
