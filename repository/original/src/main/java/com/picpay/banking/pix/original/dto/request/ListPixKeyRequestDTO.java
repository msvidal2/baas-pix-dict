package com.picpay.banking.pix.original.dto.request;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListPixKeyRequestDTO {

    private List<PixKeyRequestDTO> listKey;

    public static ListPixKeyRequestDTO from(final PixKey pixKey) {
        var pixKeyRequestDTO = PixKeyRequestDTO.builder()
            .key(pixKey.getKey())
            .responsibleKey(String.valueOf(pixKey.getTaxId()))
            .build();

        return ListPixKeyRequestDTO.builder()
            .listKey(Arrays.asList(pixKeyRequestDTO))
            .build();
    }

}
