package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
public class ListPixKeyResponseDTO {

    private List<ListPixKeyDTO> response;

    public Collection<PixKey> toDomain() {
        return getResponse().stream().map(this::getPixKey).collect(Collectors.toList());
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