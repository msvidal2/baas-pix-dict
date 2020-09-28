package com.picpay.banking.jdpi.converter;

import com.picpay.banking.jdpi.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListKeyResponseDTO;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.PixKey;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ListPixKeyConverter implements DataConverter<ListPixKeyResponseDTO, Collection<PixKey>> {

    @Override
    public Collection<PixKey> convert(final ListPixKeyResponseDTO from) {
        return from.getChavesAssociadas().stream().map(this::getPixKey).collect(Collectors.toList());
    }

    private PixKey getPixKey(final ListKeyResponseDTO listKeyResponseDTO) {
        return PixKey.builder()
            .key(listKeyResponseDTO.getChave())
            .name(listKeyResponseDTO.getNome())
            .fantasyName(listKeyResponseDTO.getNomeFantasia())
            .createdAt(listKeyResponseDTO.getDtHrCriacaoChave())
            .startPossessionAt(listKeyResponseDTO.getDtHrInicioPosseChave())
            .claim(listKeyResponseDTO.getReivindicacao() != null ? ClaimType.resolve(listKeyResponseDTO.getReivindicacao()) : null).build();
    }
}
