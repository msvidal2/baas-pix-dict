package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
public class ListPixKeyResponseDTO {

    private List<ListPixKeyDTO> response;

    public List<PixKey> toDomain() {
        return getResponse()
                .stream()
                .map(ListPixKeyDTO::toDomain)
                .collect(Collectors.toList());
    }

}