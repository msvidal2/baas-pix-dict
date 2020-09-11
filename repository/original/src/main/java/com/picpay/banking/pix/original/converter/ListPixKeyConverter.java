package com.picpay.banking.pix.original.converter;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.original.dto.response.ListPixKeyDTO;
import com.picpay.banking.pix.original.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ListPixKeyConverter implements DataConverter<ResponseWrapperDTO<ListPixKeyResponseDTO>, Collection<PixKey>> {

    @Override
    public Collection<PixKey> convert(final ResponseWrapperDTO<ListPixKeyResponseDTO> from) {
        return from.getData().getResponse().stream().map(this::getPixKey).collect(Collectors.toList());
    }

    private PixKey getPixKey(final ListPixKeyDTO listPixKeyDTO) {
        return PixKey.builder()
            .key(listPixKeyDTO.getKeyCod())
            .ispb(Integer.valueOf(listPixKeyDTO.getIspb()))
            .name(listPixKeyDTO.getName())
            .fantasyName(listPixKeyDTO.getName()) /** Revisar os de--para **/
            .accountNumber(listPixKeyDTO.getAccount())
            .cpfCnpj(Long.valueOf(listPixKeyDTO.getTaxId()))
            .accountOpeningDate(listPixKeyDTO.getAccountOpeningDate())
            .startPossessionAt(listPixKeyDTO.getCreationDate()).build();
    }
}
